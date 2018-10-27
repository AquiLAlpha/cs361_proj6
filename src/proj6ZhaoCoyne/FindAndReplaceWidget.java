package proj6ZhaoCoyne;

import javafx.stage.Stage;

import java.util.*;

public class FindAndReplaceWidget {

    private Iterator<int[]> indices = new ArrayList<int[]>().iterator();
    private String target;

    public FindAndReplaceWidget() {

    }

    public FindAndReplaceWidget(String source, String target) {

        indices = findAllIndices(source, target).iterator();
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isEmpty() {
        return !indices.hasNext();
    }

    /**
     * @param source
     * @param substring
     * @return the start and end indices of the target string in the source
     */
    private int[] indicesOf(String source, String substring) {
        int startOfSubstring = source.indexOf(substring);
        if(startOfSubstring == -1 || "".equals(substring)) { return new int[] {-1, -1}; }
        return new int[] {startOfSubstring, startOfSubstring + substring.length()};
    }

    private List<int[]> findAllIndicesHelper(
            List<int[]> idxAccumulator, int numPreviousChars,
            String source, String substring) {

        int[] indicesOfSubstring = this.indicesOf(source, substring);
        int start = indicesOfSubstring[0];
        int end   = indicesOfSubstring[1];

        if (start == -1) {
            return idxAccumulator;
        }
        idxAccumulator.add(new int[] {start + numPreviousChars, end + numPreviousChars});
        String unsearchedText = source.substring(end, source.length());
        int numCharsSearched  = numPreviousChars + end;
        return findAllIndicesHelper(idxAccumulator, numCharsSearched, unsearchedText, substring);
    }

    private List<int[]> findAllIndices(String source, String substring) {
        List<int[]> result = new ArrayList<>();
        return findAllIndicesHelper(result, 0, source, substring);
    }

    public void createIteratorFrom(String source, String target) {
        this.indices = findAllIndices(source, target).iterator();
    }

    // returns [-1, -1] if there is no next value
    public int[] getNextTargetLocation() {
        if(!this.isEmpty()) {
            return indices.next();
        }
        return null;
    }


    public Iterator<int[]> getIterator() {
        return indices;
    }

    public void showWindow(Stage parent) {

    }


    public static void main(String[] args) {
        //String test = "half";
        //String target = "hi";
        //System.out.println(test.indexOf(target));
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
