/*
 * Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
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

public class Store3MultiStripe {
  private static final URI SERVER_URI = URI.create("terracotta://localhost");
  private static final String STORE_NAME = "mySampleStore03";
  private static final StringCellDefinition LAPTOP_ID_CELL = CellDefinition.defineString("laptopId");
  private static final BoolCellDefinition WORKS_FROM_HOME_CELL = CellDefinition.defineBool("worksFromHome");
  private static final String SERVER_RESOURCE = "primary-server-resource";

  public static void main(String[] args) throws StoreException {

    try (DatasetManager datasetManager = DatasetManager.clustered(SERVER_URI).build()) {
      // clean-up (delete) sample dataset from any previous run of this sample program
      datasetManager.destroyDataset(STORE_NAME);

      // create and use a dataset
      DatasetConfiguration datasetConfiguration = datasetManager.datasetConfiguration()
          .offheap(SERVER_RESOURCE)
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
