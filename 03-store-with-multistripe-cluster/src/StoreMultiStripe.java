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
import com.terracottatech.store.Dataset;
import com.terracottatech.store.DatasetWriterReader;
import com.terracottatech.store.Record;
import com.terracottatech.store.StoreException;
import com.terracottatech.store.Type;
import com.terracottatech.store.configuration.DatasetConfiguration;
import com.terracottatech.store.definition.BoolCellDefinition;
import com.terracottatech.store.definition.CellDefinition;
import com.terracottatech.store.definition.StringCellDefinition;
import com.terracottatech.store.indexing.IndexSettings;
import com.terracottatech.store.manager.DatasetManager;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class StoreMultiStripe {
  private static final String TERRACOTTA_URI_ENV = "TERRACOTTA_SERVER_URL";
  private static final String DEFAULT_TSA_PORT = "9410";
  private static final String STORE_NAME = "mySampleStore03";
  private static final StringCellDefinition LAPTOP_ID_CELL = CellDefinition.defineString("laptopId");
  private static final BoolCellDefinition WORKS_FROM_HOME_CELL = CellDefinition.defineBool("worksFromHome");
  private static final String SERVER_RESOURCE = "main";
  private static final String DISK_RESOURCE = "main";
  private static final String DEFAULT_SERVER_URI_STR = "terracotta://localhost:" + DEFAULT_TSA_PORT;
  private static final String SERVER_URI_STR = System.getenv(TERRACOTTA_URI_ENV) == null ? DEFAULT_SERVER_URI_STR : System.getenv(TERRACOTTA_URI_ENV);

  public static void main(String[] args) throws StoreException {

    try (DatasetManager datasetManager = DatasetManager.clustered(URI.create(SERVER_URI_STR)).build()) {
      // clean-up (delete) sample dataset from any previous run of this sample program
      datasetManager.destroyDataset(STORE_NAME);

      // create and use a dataset
      DatasetConfiguration datasetConfiguration = datasetManager.datasetConfiguration()
          .offheap(SERVER_RESOURCE)
          .disk(DISK_RESOURCE)
          .index(LAPTOP_ID_CELL, IndexSettings.BTREE)
          .index(WORKS_FROM_HOME_CELL, IndexSettings.BTREE)
          .build();
      datasetManager.newDataset(STORE_NAME, Type.LONG, datasetConfiguration);

      try (Dataset<Long> rawDataset = datasetManager.getDataset(STORE_NAME, Type.LONG)) {

        rawDataset.getIndexing().getLiveIndexes().stream()
            .forEach(index -> System.out.println("Index on cell '" + index.on().name() + "' is live now"));

        DatasetWriterReader<Long> myDataset = rawDataset.writerReader();

        Random random = new Random();
        ArrayList<Long> employeeIds = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
          long employeeId = Math.abs(random.nextLong());
          employeeIds.add(employeeId);
          String laptopId = UUID.randomUUID().toString();
          Boolean worksFromHome = Math.random() < 0.5;

          System.out.println("Storing record with employeeId : " + employeeId +
              " as key and laptopId : " + laptopId +
              " and worksFromHome : " + worksFromHome +
              " as cells");

          myDataset.add(employeeId, LAPTOP_ID_CELL.newCell(laptopId), WORKS_FROM_HOME_CELL.newCell(worksFromHome));
        }

        // Remove records for all the employees who are working from home
        for (Long employeeId : employeeIds) {
          Optional<Record<Long>> recordOptional = myDataset.on(employeeId).iff(WORKS_FROM_HOME_CELL.isTrue()).delete();

          if (recordOptional.isPresent()) {
            Long empId = recordOptional.get().getKey();
            String laptopId = recordOptional.get().get(LAPTOP_ID_CELL).get();
            System.out.println("Deleted record with employeeId : " + empId + " and laptopId : " + laptopId);
          }
        }
      }
    }
  }
}
