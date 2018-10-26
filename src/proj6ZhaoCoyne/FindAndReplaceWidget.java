package proj6ZhaoCoyne;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FindAndReplaceWidget {


    /**
     * @param source
     * @param substring
     * @return the start and end indices of the target string in the source
     */
    public int[] indicesOf(String source, String substring) {
        int startOfSubstring = source.indexOf(substring);
        if(startOfSubstring == -1) { return new int[] {-1, -1}; }
        return new int[] {startOfSubstring, startOfSubstring + substring.length()};
    }

    private List<int[]> findAllIndicesHelper(
            List<int[]> idxAccumulator, int numPreviousChars,
            String source, String substring) {
        int[] indicesOfSubstring = indicesOf(source, substring);
        int start = indicesOfSubstring[0];
        int end = indicesOfSubstring[1];

        if (start == -1) {
            return idxAccumulator;
        }
        idxAccumulator.add(new int[] {start + numPreviousChars, end + numPreviousChars});
        String unsearchedString = source.substring(end, source.length());
        int numCharsSearched = numPreviousChars + end - start;
        return findAllIndicesHelper(idxAccumulator, numCharsSearched, unsearchedString, substring);
    }

    public List<int[]> findAllIndices(String source, String substring) {
        List<int[]> result = new ArrayList<>();
        return findAllIndicesHelper(result, 0, source, substring);
    }



    public static void main(String[] args) {
        FindAndReplaceWidget widget = new FindAndReplaceWidget();
        String testStr = "Hello, this is a test this";
        int[] result = widget.indicesOf(testStr, "this");
        System.out.printf("%s, %s", result[0], result[1]);

        System.out.printf("%n%s", testStr.substring(result[0], result[1]));
        System.out.printf("%n%s", testStr.substring(result[1], testStr.length()));

        List<int[]> result2 = widget.findAllIndices(testStr, "this");

        for(int[] arr : result2) {
            System.out.printf("%n[%s,%s]", arr[0], arr[1]);
        }

    }

}
