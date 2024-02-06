class Sorter {

    public static void sortStrings(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j >= 1; j--) {
                if (isGreaterThan(arr[j - 1], arr[j])) {
                    swap(arr, j, j - 1);
                } else {
                    break;
                }
            }
        }
    }

    public static void swap(String[] arr, int i, int j) {
        String temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static boolean isGreaterThan(String str1, String str2) {
        // check for the three conditions
        if (str2.isEmpty()) return true;
        else if (str1.charAt(0) > str2.charAt(0)) return true;
        else return str1.charAt(0) == str2.charAt(0) && str1.charAt(1) > str2.charAt(1);
    }
}
