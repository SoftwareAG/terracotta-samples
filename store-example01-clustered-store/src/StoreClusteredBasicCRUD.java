/*
 * Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

import com.terracottatech.store.Dataset;
import com.terracottatech.store.DatasetWriterReader;
import com.terracottatech.store.StoreException;
import com.terracottatech.store.Type;
import com.terracottatech.store.configuration.DatasetConfiguration;
import com.terracottatech.store.definition.CellDefinition;
import com.terracottatech.store.definition.IntCellDefinition;
import com.terracottatech.store.definition.StringCellDefinition;
import com.terracottatech.store.manager.DatasetManager;

import java.net.URI;

public class StoreClusteredBasicCRUD {
  private static final URI SERVER_URI = URI.create("terracotta://localhost");
  private static final String STORE_NAME = "mySampleStore01";
  private static final IntCellDefinition FAVORITE_NUMBER_CELL = CellDefinition.defineInt("favoriteNumber");
  private static final StringCellDefinition NAME_CELL = CellDefinition.defineString("name");
  private static final String SERVER_RESOURCE = "primary-server-resource";

  public static void main(String[] args) throws StoreException {
    try (DatasetManager datasetManager = DatasetManager.clustered(SERVER_URI).build()) {
      // clean-up (delete) sample dataset from any previous run of this sample program
      datasetManager.destroyDataset(STORE_NAME);

      // create and use a dataset
      DatasetConfiguration offheapResource = datasetManager.datasetConfiguration().offheap(SERVER_RESOURCE).build();
      datasetManager.newDataset(STORE_NAME, Type.LONG, offheapResource);

      try (Dataset<Long> rawDataset = datasetManager.getDataset(STORE_NAME, Type.LONG)) {
        DatasetWriterReader<Long> myDataset = rawDataset.writerReader();
        myDataset.add(123L, NAME_CELL.newCell("George"), FAVORITE_NUMBER_CELL.newCell(42));
        myDataset.get(123L).ifPresent(
            rec -> System.out.println("Stored and retrieved a record with favorite number: " + rec.get(FAVORITE_NUMBER_CELL).get()));

        myDataset.delete(123L);
        System.out.println("Deleted same record");
      }
    }
  }
}
