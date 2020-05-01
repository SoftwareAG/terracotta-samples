import com.terracottatech.store.Dataset;
import com.terracottatech.store.DatasetWriterReader;
import com.terracottatech.store.StoreException;
import com.terracottatech.store.Type;
import com.terracottatech.store.definition.CellDefinition;
import com.terracottatech.store.definition.IntCellDefinition;
import com.terracottatech.store.definition.StringCellDefinition;
import com.terracottatech.store.manager.ConfigurationMode;
import com.terracottatech.store.manager.DatasetManager;
import com.terracottatech.store.manager.DatasetManagerConfiguration;
import com.terracottatech.store.manager.XmlConfiguration;

import java.net.URL;

public class StoreClusteredXML {
  private static final String STORE_NAME = "mySampleStore05";
  private static final IntCellDefinition FAVORITE_NUMBER_CELL = CellDefinition.defineInt("favoriteNumber");
  private static final StringCellDefinition NAME_CELL = CellDefinition.defineString("name");

  public static void main(String[] args) throws StoreException {
    URL xmlConfigurationUrl = StoreClusteredXML.class.getResource("/clustered.xml");

    DatasetManagerConfiguration xmlConfiguration = XmlConfiguration.parseDatasetManagerConfig(xmlConfigurationUrl);
    try (DatasetManager datasetManager = DatasetManager.using(xmlConfiguration, ConfigurationMode.AUTO)) {
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