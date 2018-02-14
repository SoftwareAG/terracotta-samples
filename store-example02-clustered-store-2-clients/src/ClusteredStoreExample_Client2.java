/*
 * Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

import com.terracottatech.store.Dataset;
import com.terracottatech.store.DatasetWriterReader;
import com.terracottatech.store.StoreException;
import com.terracottatech.store.Type;
import com.terracottatech.store.definition.CellDefinition;
import com.terracottatech.store.definition.IntCellDefinition;
import com.terracottatech.store.manager.DatasetManager;

import java.io.IOException;
import java.net.URI;

import static com.terracottatech.store.UpdateOperation.allOf;
import static com.terracottatech.store.UpdateOperation.write;

public class ClusteredStoreExample_Client2 {
  private static final URI SERVER_URI = URI.create("terracotta://localhost");
  private static final String STORE_NAME = "mySampleStore01";
  private static final IntCellDefinition FAVORITE_NUMBER_CELL = CellDefinition.defineInt("favoriteNumber");
  private static final IntCellDefinition AGE_CELL = CellDefinition.defineInt("age");

  public static void main(String[] args) throws StoreException, IOException {
    try (DatasetManager datasetManager = DatasetManager.clustered(SERVER_URI).build();
         Dataset<Long> rawDataset = datasetManager.getDataset(STORE_NAME, Type.LONG)) {

      System.out.println("\n\nConnected to Dataset.\n");

      DatasetWriterReader<Long> myDataset = rawDataset.writerReader();

      myDataset.update(123L, write(FAVORITE_NUMBER_CELL, 22));
      myDataset.update(123L, allOf(write(AGE_CELL, 35), write(FAVORITE_NUMBER_CELL, 22)));

      myDataset.get(123L).ifPresent(rec ->
          System.out.println("Updated record with favorite number: " + rec.get(FAVORITE_NUMBER_CELL).get() + " and age: " + rec.get(AGE_CELL).get())
      );

      System.out.println("\nFinished.\n");
    }
  }
}
