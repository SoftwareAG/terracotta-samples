import static com.terracottatech.store.Cell.cell;
import com.terracottatech.store.CellSet;
import com.terracottatech.store.ComplexData;
import com.terracottatech.store.ComplexRecord;
import com.terracottatech.store.Dataset;
import com.terracottatech.store.DatasetWriterReader;
import com.terracottatech.store.Record;
import com.terracottatech.store.StoreException;
import com.terracottatech.store.StoreList;
import com.terracottatech.store.StoreMap;
import com.terracottatech.store.Type;
import com.terracottatech.store.configuration.DatasetConfiguration;
import com.terracottatech.store.definition.ComplexDataParseException;
import com.terracottatech.store.definition.ComplexDataParser;
import com.terracottatech.store.manager.DatasetManager;

import java.net.URI;
import java.util.Optional;

public class ComplexDataTypeSample {

    private static final String TERRACOTTA_URI_ENV = "TERRACOTTA_SERVER_URL";
    private static final String DEFAULT_TSA_PORT = "9410";
    private static final String DEFAULT_SERVER_URI_STR = "terracotta://localhost:" + DEFAULT_TSA_PORT;
    private static final String DATASET_NAME = "mySampleDataset";
    private static final String SERVER_RESOURCE = "main";
    private static final String SERVER_URI_STR = System.getenv(TERRACOTTA_URI_ENV) == null ? DEFAULT_SERVER_URI_STR : System.getenv(TERRACOTTA_URI_ENV);

    public static void main(String[] args) throws ComplexDataParseException, StoreException {

        try (DatasetManager datasetManager = DatasetManager.clustered(URI.create(SERVER_URI_STR)).build()) {
            // clean-up (delete) sample dataset from any previous run of this sample program
            datasetManager.destroyDataset(DATASET_NAME);

            // create and use a dataset
            DatasetConfiguration offheapResource = datasetManager.datasetConfiguration().offheap(SERVER_RESOURCE).build();
            datasetManager.newDataset(DATASET_NAME, Type.LONG, offheapResource);

            try (Dataset<Long> rawDataset = datasetManager.getDataset(DATASET_NAME, Type.LONG)) {

                DatasetWriterReader<Long> myDataset = rawDataset.writerReader();
                myDataset.add(123L, originalCellSet());

                System.out.println("int value retrieved from Dataset is : "
                        + myDataset.get(123L).get().asComplex().find(".int").get(0).get().value());
                System.out.println("String value retrieved from list of cells from Dataset is : "
                        + myDataset.get(123L).get().asComplex().find(".list_string.[]").get(1).get().value());
                System.out.println("size of list_double retrieved from Dataset is : "
                        + myDataset.get(123L).get().asComplex().find(".list_double.[]").size());

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

                /*
                 * Find operaions
                 * find(spec) Selection querying for efficient selecting of arbitrary data inside CDTs by type, position, and key, in various combinations
                 */

                // We can use find function for ComplexRecord and Record both
                // Find function using ComplexRecord
                System.out.println("\nAge retrieved using ComplexRecord is : " + ComplexRecord.mutableRecord("rec", StoreMap.newMap().with("map", map))
                        .find(".map.age").get(0).get().value());

                // Find function using Record
                Record<String> record = ComplexRecord.mutableRecord("rec", StoreMap.newMap().with("map", map)).toRecord();
                System.out.println("Age retrieved using Record is : " + record.asComplex().find(".map.age").get(0).get().value());
                System.out.println("Get first age : " + record.asComplex().find(".map.age").first().get().value());
                System.out.println("Get last age : " + record.asComplex().find(".map.age").last().get().value());
                System.out.println("Get sole age : " + record.asComplex().find(".map.age").sole().value());
                System.out.println("Get age from end : " + record.asComplex().find(".map.age").get(-1).get().value());
                System.out.println("Size of map is : " + record.asComplex().find(".map.[]").size());

                myDataset.add(0L, cell("name", "john"),
                        cell("age", 10),
                        cell("myMap", map),
                        cell("myList", list));

                // Find function using Dataset
                System.out.println("\nAge retrieved using dataset is : " + myDataset.get(0L).get().asComplex().find(".myMap.age").get(0).get().value());
                System.out.println("Get first age using dataset is : " + myDataset.get(0L).get().asComplex().find(".myMap.age").first().get().value());
                System.out.println("Get last age using dataset is : " + myDataset.get(0L).get().asComplex().find(".myMap.age").last().get().value());
                System.out.println("Get sole age using dataset is : " + myDataset.get(0L).get().asComplex().find(".myMap.age").sole().value());
                System.out.println("Get age from end using dataset is : " + myDataset.get(0L).get().asComplex().find(".myMap.age").get(-1).get().value());
                System.out.println("Size of map using dataset is : " + myDataset.get(0L).get().asComplex().find(".myMap.[]").size());

                Record<String> rec2 = ComplexRecord.mutableRecord("foofoo", map).toRecord();
                System.out.println("\nFirst value in map is : " + rec2.asComplex().find(".[]").first().get().value());

                StoreList newList = StoreList.newList().with(10, 12, 14, 16);
                Record<String> rec3 = ComplexRecord.mutableRecord("foo", StoreMap.newMap().with("list", newList)).toRecord();

                Integer value = rec3.asComplex().find(".list.[]").integers().get(2).get().intValue();
                System.out.println("\nValue in list at 2nd index is 14 ? : " + value.equals(14));
                Integer size = rec3.asComplex().find(".list.[]").integers().size();
                System.out.println("Size of the list is : " + size);

                /*
                 * Operations related to Booleans in a map
                 */

                StoreMap mapBooleans = StoreMap.newMap().with("bool1", true)
                        .with("char", '2')
                        .with("bool2", false)
                        .with("list", StoreList
                                .newList().with(false)
                                .with(true)
                                .with(false));
                Record<String> rec4 = ComplexRecord.mutableRecord("rec", StoreMap.newMap().with("map", mapBooleans)).toRecord();
                System.out.println("\nSize of Boolean value is : " + rec4.asComplex().find(".map.[]").booleans().size());
                System.out.println("Size of Boolean list in map is : " + rec4.asComplex().find(".map.list.[]").booleans().size());

                myDataset.add(1L, cell("booleanMap", mapBooleans));
                System.out.println("\nSize of Boolean value retrieved from dataset is : " + myDataset.get(1L).get().asComplex().find(".booleanMap.[]").booleans().size());
                System.out.println("Size of Boolean list in map retrieved from dataset is : " + myDataset.get(1L).get().asComplex().find(".booleanMap.list.[]").booleans().size());

                /*
                 * Operations related to Characters in a map
                 */

                StoreMap mapChars = StoreMap.newMap().with("bool1", 'a')
                        .with("char", true)
                        .with("bool2", 'f')
                        .with("list", StoreList
                                .newList().with('a', 'c', 'f', 'b'));

                Record<String> rec5 = ComplexRecord.mutableRecord("rec", StoreMap.newMap().with("map", mapChars)).toRecord();
                System.out.println("\nSize of Char is : " + rec5.asComplex().find(".map.[]").chars().size());
                System.out.println("Size of Char list less than or equal to 'a' : " + rec5.asComplex().find(".map.[]").chars().lessThanOrEqual('a').size());
                System.out.println("Size of Char list greater than or equal to 'f' : " + rec5.asComplex().find(".map.[]").chars().greaterThanOrEqual('a').size());

                myDataset.add(2L, cell("charsMap", mapChars));
                System.out.println("\nSize of Char list less than or equal to 'a' retrieved from dataset is : " + myDataset.get(2L).get().asComplex().find(".charsMap.[]").chars().lessThanOrEqual('a').size());
                System.out.println("Size of Char list greater than or equal to 'f' retrieved from dataset is : " + myDataset.get(2L).get().asComplex().find(".charsMap.list.[]").chars().greaterThanOrEqual('a').size());

                /*
                 * Operations related to Strings in a map
                 */

                StoreMap mapStrings = StoreMap.newMap().with("1", "a")
                        .with("char", true)
                        .with("2", "f")
                        .with("list", StoreList.newList().with("a", "c", "f", "b"));

                Record<String> rec6 = ComplexRecord.mutableRecord("rec", StoreMap.newMap().with("map", mapStrings)).toRecord();
                System.out.println("\nSize of string is : " + rec6.asComplex().find(".map.[]").strings().size());
                System.out.println("Size of string list in map is : " + rec6.asComplex().find(".map.list.[]").strings().size());

                myDataset.add(3L, cell("stringsMap", mapStrings));
                System.out.println("\nSize of string retrieved from dataset is : " + myDataset.get(3L).get().asComplex().find(".stringsMap.[]").strings().size());
                System.out.println("Size of string list in map retrieved from dataset is : " + myDataset.get(3L).get().asComplex().find(".stringsMap.list.[]").strings().size());

                /*
                 * Operations related to Bytes in a map
                 */

                StoreMap mapBytes =
                        StoreMap.newMap().with("1", new byte[]{0})
                                .with("char", true)
                                .with("2", new byte[]{2})
                                .with("list", StoreList.newList()
                                        .with(new byte[]{0}, new byte[]{10}, new byte[]{5}, new byte[]{20}));
                Record<String> rec7 = ComplexRecord.mutableRecord("rec", StoreMap.newMap().with("map", mapBytes)).toRecord();
                System.out.println("\nSize of Byte is : " + rec7.asComplex().find(".map.[]").bytes().size());
                System.out.println("Size of Byte list in map is : " + rec7.asComplex().find(".map.list.[]").bytes().size());

                myDataset.add(4L, cell("bytesMap", mapBytes));
                System.out.println("\nSize of Byte retrieved from dataset is : " + myDataset.get(4L).get().asComplex().find(".bytesMap.[]").bytes().size());
                System.out.println("Size of Byte list in map retrieved from dataset is : " + myDataset.get(4L).get().asComplex().find(".bytesMap.list.[]").bytes().size());

                /*
                 * Operations related to Integers in a map
                 */

                StoreMap mapInt = StoreMap.newMap().with("1", 1)
                        .with("char", true)
                        .with("2", 2)
                        .with("list", StoreList.newList()
                                .with(1, 5, 32, 8));

                Record<String> rec8 = ComplexRecord.mutableRecord("rec", StoreMap.newMap().with("map", mapInt)).toRecord();
                System.out.println("\nSize of Integer is : " + rec8.asComplex().find(".map.[]").integers().size());
                System.out.println("Size of Integer list in map is : " + rec8.asComplex().find(".map.list.[]").integers().size());
                System.out.println("First Integer value is : " + rec8.asComplex().find(".map.list.[]").integers().first().get());
                System.out.println("Integer value at index 1st is : " + rec8.asComplex().find(".map.list.[]").integers().get(1).get());
                System.out.println("Number of Integers greater than 5 are : " + rec8.asComplex().find(".map.list.[]").integers().greaterThan(5).size());
                System.out.println("Number of Integers greater than or equal to 5 are : " + rec8.asComplex().find(".map.list.[]").integers().greaterThanOrEqual(5).size());
                System.out.println("Number of Integers less than or equal to 5 are : " + rec8.asComplex().find(".map.list.[]").integers().lessThanOrEqual(5).size());
                System.out.println("Number of Integers less than 5 are : " + rec8.asComplex().find(".map.list.[]").integers().lessThan(5).size());
                System.out.println("Sum of Integer list is : " + rec8.asComplex().find(".map.list.[]").integers().sum());

                myDataset.add(5L, cell("intMap", mapInt));
                System.out.println("\nSize of Integer retrieved from dataset is : " + myDataset.get(5L).get().asComplex().find(".intMap.[]").integers().size());
                System.out.println("Size of Integer list in map retrieved from dataset is : " + myDataset.get(5L).get().asComplex().find(".intMap.list.[]").integers().size());
                System.out.println("First Integer value retrieved from dataset is : " + myDataset.get(5L).get().asComplex().find(".intMap.list.[]").integers().first().get());
                System.out.println("Integer value at index 1st retrieved from dataset is : " + myDataset.get(5L).get().asComplex().find(".intMap.list.[]").integers().get(1).get());
                System.out.println("Number of Integers greater than 5 retrieved from dataset are : " + myDataset.get(5L).get().asComplex().find(".intMap.list.[]").integers().greaterThan(5).size());
                System.out.println("Number of Integers greater than or equal to 5 retrieved from dataset are : " + myDataset.get(5L).get().asComplex().find(".intMap.list.[]").integers().greaterThanOrEqual(5).size());
                System.out.println("Number of Integers less than or equal to 5 retrieved from dataset are : " + myDataset.get(5L).get().asComplex().find(".intMap.list.[]").integers().lessThanOrEqual(5).size());
                System.out.println("Number of Integers less than 5 retrieved from dataset are : " + myDataset.get(5L).get().asComplex().find(".intMap.list.[]").integers().lessThan(5).size());
                System.out.println("Sum of Integer list retrieved from dataset is : " + myDataset.get(5L).get().asComplex().find(".intMap.list.[]").integers().sum());

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