import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

public class ProblemStreamFromFile implements Problem {
    @Override
    public void main(String[] args) throws Exception {
        String filename = "./resources/ForProblemStreamFromFile.txt";

        //User supplier to produce multiple streams
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            System.out.println(getMaxLength(stream));
        } catch (IOException ex) {
            System.out.println(ex);
        }

        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            System.out.println(getMaxString(stream));
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    //Should return mahendrasinghdhoni1
    private String getMaxString(Stream<String> stream) {
        //String maxLenghtString = stream.max(Comparator.comparing(String::length)).get();
        String maxString = stream.reduce((string1, string2) -> string1.length()>=string2.length() ? string1 : string2).get();
        System.out.println("Reduced: "+maxString);
        return maxString;
    }

    //should return 19
    private int getMaxLength(Stream<String> stream) {
        OptionalInt maxLength = stream.mapToInt(String::length).max();
        return maxLength.getAsInt();
    }
}
