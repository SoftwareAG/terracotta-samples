import static com.terracottatech.store.Cell.cell;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

import com.terracottatech.store.CellSet;
import com.terracottatech.store.ComplexData;
import com.terracottatech.store.Dataset;
import com.terracottatech.store.DatasetWriterReader;
import com.terracottatech.store.Record;
import com.terracottatech.store.StoreException;
import com.terracottatech.store.StoreList;
import com.terracottatech.store.StoreMap;
import com.terracottatech.store.Type;
import com.terracottatech.store.TypedValue;
import com.terracottatech.store.configuration.DatasetConfiguration;
import com.terracottatech.store.definition.ComplexDataParseException;
import com.terracottatech.store.definition.ComplexDataParser;
import com.terracottatech.store.manager.DatasetManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public class ComplexDataTypeSample {

  private static final String TERRACOTTA_URI_ENV = "TERRACOTTA_SERVER_URL";
  private static final String DEFAULT_TSA_PORT = "9410";
  private static final String DEFAULT_SERVER_URI_STR = "terracotta://localhost:" + DEFAULT_TSA_PORT;
  private static final String DATASET_NAME = "mySampleDataset";
  private static final String SHINOBI_DATASET = "myShinobiDataset";
  private static final String SERVER_RESOURCE = "main";
  private static final String SERVER_URI_STR = System.getenv(TERRACOTTA_URI_ENV) == null ? DEFAULT_SERVER_URI_STR : System.getenv(TERRACOTTA_URI_ENV);

  public static void main(String[] args) throws ComplexDataParseException, StoreException {

    try (DatasetManager datasetManager = DatasetManager.clustered(URI.create(SERVER_URI_STR)).build()) {
      // clean-up (delete) sample dataset from any previous run of this sample program
      datasetManager.destroyDataset(DATASET_NAME);

      DatasetConfiguration offheapResource = datasetManager.datasetConfiguration().offheap(SERVER_RESOURCE).build();
      datasetManager.newDataset(SHINOBI_DATASET, Type.STRING, offheapResource);

      // create and use a dataset
      try (Dataset<String> dataset = datasetManager.getDataset(SHINOBI_DATASET, Type.STRING)) {
        DatasetWriterReader<String> access = dataset.writerReader();

        /*
         * Creating Mutable StoreList and StoreMap using newList() and newMap() methods
         * @return store list
         * @return store map
         */

        StoreList list = StoreList.newList()
                .with("john").with('R')
                .with(10)
                .with(true)
                .with(100101010L)
                .with(98.6d)
                .with(new byte[]{1, 2, 3, 4});

        StoreMap map = StoreMap.newMap()
                .with("name", "john")
                .with("initial", 'R')
                .with("age", 10)
                .with("alive", true)
                .with("marker", 100101010L)
                .with("temp", 98.6d)
                .with("img", new byte[]{1, 2, 3, 4});

        System.out.println("StoreList is : " + list);
        System.out.println("StoreMap is : " + map);

        /*
         * Create a StoreMap and StoreList by parsing the String.
         * @return store list
         * @return store map
         */

        StoreMap mapUsingMapOF = StoreMap.mapOf("{ 'A' : 1, 'F' : 1}");
        StoreList listUsingListOf = StoreList.listOf("[ 1 , 'A' ]");

        System.out.println("\nStoreList using listOf method : " + listUsingListOf);
        System.out.println("StoreMap using mapOf method : " + mapUsingMapOF);

        /*
         * Create a map from a {@link CellSet}.
         * Names of cells become keys, type/value pairs become values.
         * @return store map
         */

        StoreMap mapUsingCellMapOf = StoreMap.mapOf(originalCellSet());
        System.out.println("\nMap using mapOf(Cellset) method : " + mapUsingCellMapOf);

        /*
         * A new deep-copy mutable instance can be created from any instance via the mutableCopy() method.
         * Make deep copy of this map and list, ensuring it is mutable.
         * @return mutable copy.
         */

        StoreList mutableList = list.mutableCopy();
        StoreMap mutableMap = map.mutableCopy();

        System.out.println("\nMutable StoreList is : " + mutableList);
        System.out.println("Mutable StoreMap is : " + mutableMap);

        /*
         * Text representation of list and map using toString(boolean skipWhitespace) method
         * @param skipWhitespace line breaks and indention, or not.
         * @return String version of this object.
         */

        String listAsString = list.toString(false);
        String mapAsString = map.toString(false);

        System.out.println("\nStoreList in text representation : " + listAsString);
        System.out.println("StoreMap in text representation : " + mapAsString);

        /*
         * Construct a single StoreList and StoreMap by parser from the provided String
         * @return StoreMap.
         * @return StoreList.
         */

        StoreList convertedList = StoreList.listOf(listAsString);
        StoreMap convertedMap = StoreMap.mapOf(mapAsString);

        System.out.println("\nList converted from String is same as original one ? : " + convertedList.equals(list));
        System.out.println("Map converted from String is same as original one ? : " + convertedMap.equals(map));

        /*
         * Creating a parser for a specific string and retrieving values of the Map.
         */

        ComplexDataParser parser = ComplexData.parser(mapAsString);
        for (StoreMap p = parser.nextMap(); p != null; p = parser.nextMap()) {
          Optional<String> name = p.find(".name").strings().first();
          Optional<Integer> age = p.find(".age").integers().first();

          // Similarly we can retrieve other values from StoreList and StoreMap.
          System.out.println("\nName retrieved by Complex data parser is : " + name.get());
          System.out.println("Age retrieved by Complex data parser is : " + age.get());
        }

        InputStream is = ComplexDataTypeSample.class.getResourceAsStream("/resources/shinobi.json");
        ComplexDataParser complexDataParserarser = ComplexData.parser(new InputStreamReader(is));
        StoreMap p = complexDataParserarser.nextMap();
        StoreList allShinobi = (StoreList) p.get("shinobi").value();

        for (TypedValue<?> shinobi : allShinobi) {
          Optional<StoreMap> m = shinobi.ifMap();
          if (m.isPresent()) {
            StoreMap storeMap = m.get().mutableCopy();
            access.add(storeMap.get("name").ifString().get(), storeMap.asCellSet());
          }
        }

        out.print("\nTotal number of shinobi are : " + access.records().count());
        Record<String> record = access.records().findFirst().get();
        out.println("\nfirst record is : " + record);

        // Lightning type Shinobi
        List<String> lightning = access.records().filter(Record.find(".type.[]")
                        .strings().contains("Lightning"))
                .map(Record::getKey).collect(toList());

        // Shinobi who are susceptible to Lightning
        List<String> hateLightning = access.records().filter(Record.find(".weaknesses.[]").strings()
                        .contains("Lightning"))
                .map(Record::getKey).collect(toList());

        // Shinobi who are strong against Earth style
        List<String> strongAgainstEarthStyle = access.records().filter(Record.find(".strength.[]")
                        .strings().contains("Earth"))
                .map(Record::getKey).collect(toList());

        out.println("\nLightening type Shinobi are : " + lightning);
        out.println("Shinobi who are susceptible to Lightening are : " + hateLightning);
        out.println("Shinobi who are strong against Earth are : " + strongAgainstEarthStyle);

        // Find function using Dataset
        out.println("\nNo. of jutsu Naruto can perform : " + access.get("Naruto").get().asComplex().find("no_of_jutsu_can_perform").get(0).get().value());
        out.println("No. of jutsu Sasuke can perform : " + access.get("Sasuke").get().asComplex().find("no_of_jutsu_can_perform").get(0).get().value());
        out.println("Is shinobi rank of Onoki is Kage ? " + access.get("Onoki").get().asComplex().find("shinobi_rank").get(0).get().value().equals("Kage"));
        out.println("Type of Kakashi is Fire ? : " + access.get("Kakashi").get().asComplex().find("type.[]").strings().contains("Lightning"));
        out.println("Naruto's first strength is : " + access.get("Naruto").get().asComplex().find("strength.[]").strings().first().get());
        out.println("Is Sasuke's strength is Water ? : " + access.get("Sasuke").get().asComplex().find("strength.[]").strings().contains("Water"));

        out.println("\nShinobi with multiple previous shinobi rank:");
        access.records().filter(Record.find(".prev_shinobi_rank.[]").size().isGreaterThanOrEqualTo(1))
                .map(Record::asComplex)
                .forEach(r -> {
                  out.println(r.getKey());
                  r.find(".prev_shinobi_rank.[].name").forEach(evo -> out.println("  became --> " + evo.value()));
                });

        out.println("\nShinobi with multiple next shinobi rank:");
        access.records().filter(Record.find(".next_shinobi_rank.[]").size().isGreaterThanOrEqualTo(1))
                .map(Record::asComplex)
                .forEach(r -> {
                  out.println(r.getKey());
                  r.find(".next_shinobi_rank.[].name").forEach(evo -> out.println("  becomes --> " + evo.value()));
                });
      }
    }
  }

  private static CellSet originalCellSet() {
    return CellSet.of(
            cell("bool", true),
            cell("char", '\u00A2'),
            cell("int", 222323),
            cell("long", 2147483647L),
            cell("double", 3.14159265D),
            cell("string", "first"),
            cell("bytes", new byte[]{(byte) 0xCA, (byte) 0xFE}),
            cell("list_bool", StoreList.newList().with(true, false, true)),
            cell("list_char", StoreList.newList().with('a', 'b', 'c')),
            cell("list_int", StoreList.newList().with(1, 2, 3)),
            cell("list_long", StoreList.newList().with(1L, 2L, 3L)),
            cell("list_double", StoreList.newList().with(1.0D, 2.0D, 3.0D)),
            cell("list_string", StoreList.newList().with("first", "second", "third")),
            cell("list_list", StoreList.newList().with(
                    StoreList.newList().with(2, 3, 5, 7),
                    StoreList.newList().with(Math.PI, Math.E))),
            cell("list_map", StoreMap.newMap().with("entry", "value"))
    );
  }
}