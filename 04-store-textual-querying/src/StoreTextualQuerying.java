/*
 * Copyright (c) 2020 Software AG, Darmstadt, Germany and/or its licensors
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
import com.terracotta.store.tql.ResultStream;
import com.terracotta.store.tql.TqlEnvironment;
import com.terracottatech.store.*;
import com.terracottatech.store.configuration.DatasetConfiguration;
import com.terracottatech.store.definition.CellDefinition;
import com.terracottatech.store.manager.DatasetManager;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import java.net.URI;

public class StoreTextualQuerying {

    private static final String TERRACOTTA_URI_ENV = "TERRACOTTA_SERVER_URL";
    private static final String DEFAULT_TSA_PORT = "9410";
    private static final String STORE_NAME = "mySampleStore04";
    private static final String SERVER_RESOURCE = "main";
    private static final String DEFAULT_SERVER_URI_STR = "terracotta://localhost:" + DEFAULT_TSA_PORT;
    private static final String SERVER_URI_STR = System.getenv(TERRACOTTA_URI_ENV) == null ? DEFAULT_SERVER_URI_STR : System.getenv(TERRACOTTA_URI_ENV);
    private static final CellDefinition<String> FIRST_NAME = CellDefinition.defineString("FirstName");
    private static final CellDefinition<String> LAST_NAME = CellDefinition.defineString("LastName");
    private static final CellDefinition<Integer> AGE = CellDefinition.defineInt("Age");

    private static void addSampleData(DatasetWriterReader<String> writerReader) {
        writerReader.add("Person1", FIRST_NAME.newCell("Bob"), LAST_NAME.newCell("Smith"), AGE.newCell(42));
        writerReader.add("Person2", FIRST_NAME.newCell("Joe"), LAST_NAME.newCell("Smith"), AGE.newCell(43));
        writerReader.add("Person3", FIRST_NAME.newCell("Larry"), LAST_NAME.newCell("Smith"), AGE.newCell(40));
        writerReader.add("Person4", FIRST_NAME.newCell("Stan"), LAST_NAME.newCell("Brown"), AGE.newCell(45));
        writerReader.add("Person5", FIRST_NAME.newCell("Bill"), LAST_NAME.newCell("Johnson"), AGE.newCell(47));
    }

    public static void main(String[] args) throws StoreException {
        Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.WARN);

        try (DatasetManager datasetManager = DatasetManager.clustered(URI.create(SERVER_URI_STR)).build()) {
            // Clean-up (delete) sample Dataset from any previous run of this sample program.
            datasetManager.destroyDataset(STORE_NAME);

            // Create and use a Dataset.
            final DatasetConfiguration datasetConfiguration = datasetManager.datasetConfiguration().offheap(SERVER_RESOURCE).build();
            datasetManager.newDataset(STORE_NAME, Type.STRING, datasetConfiguration);

            try (Dataset dataset = datasetManager.getDataset(STORE_NAME, Type.STRING)) {
                addSampleData(dataset.writerReader());

                // Get read access to the Dataset.
                final DatasetReader<String> reader = dataset.reader();

                // If the structure of the Dataset entries is unknown, use sampling to get an initial impression
                // of the structure. Be aware, however, that the sampling results may change.
                System.out.println("Sample cell definitions:");
                TqlEnvironment.sampleCellDefinitions(reader, 5)
                        .forEach(System.out::println);

                // Run a simple query and print the results.
                System.out.println("\nRun simple query (SELECT * FROM Source):");
                TqlEnvironment tqlEnv = new TqlEnvironment(reader, "Source", FIRST_NAME, LAST_NAME, AGE);
                try (ResultStream stream = tqlEnv.query("SELECT * FROM Source").stream()) {
                    stream.forEach(System.out::println);
                }

                // Investigate the result schema of the query.
                System.out.println("\nInvestigate result schema (SELECT LastName, AVG(Age) AS AvgAge FROM Source GROUP BY LastName):");
                tqlEnv.query("SELECT LastName, AVG(Age) AS AvgAge FROM Source GROUP BY LastName")
                        .cellDefinitions()
                        .forEach(System.out::println);

                // Include the key in query processing.
                System.out.println("\nAccess key in query (SELECT key, FirstName FROM Source WHERE key='Person3'):");
                tqlEnv = new TqlEnvironment(reader, "Source", FIRST_NAME, LAST_NAME, AGE)
                        .includeKeys();
                try (ResultStream stream = tqlEnv.query("SELECT key, FirstName FROM Source WHERE key='Person3'").stream()) {
                    stream.forEach(System.out::println);
                }

                // Auto-resolve ambiguous columns
                System.out.println("\nAutomatically resolve ambiguous names (SELECT * FROM Source):");
                CellDefinition<?>[] inputSchema = new CellDefinition[]{
                        CellDefinition.defineString("Age"),
                        CellDefinition.defineInt("Age")}; // ambiguous column
                tqlEnv = new TqlEnvironment(reader, "Source", TqlEnvironment.resolveAmbiguousNames(inputSchema));
                tqlEnv.query("SELECT * FROM Source")
                        .cellDefinitions()
                        .forEach(System.out::println);
                // Note that Age_STRING is always NULL as cells with name Age and type STRING are not present in the data.
                try (ResultStream stream = tqlEnv.query("SELECT * FROM Source").stream()) {
                    stream.forEach(System.out::println);
                }

                // Manually resolve ambiguous names.
                System.out.println("\nManually resolve ambiguous names (SELECT * FROM Source):");
                tqlEnv = new TqlEnvironment(reader, "Source", TqlEnvironment.as("Age_Resolved", CellDefinition.defineString("Age")), CellDefinition.defineInt("Age"));
                tqlEnv.query("SELECT * FROM Source")
                        .cellDefinitions()
                        .forEach(System.out::println);
                try (ResultStream stream = tqlEnv.query("SELECT * FROM Source").stream()) {
                    stream.forEach(System.out::println);
                }
            }
        }
    }

}