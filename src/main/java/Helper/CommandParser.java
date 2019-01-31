package Helper;

class CommandParser {

    static int[] validateAttack(String command) {
        String replaceString = command.replaceAll("\\s","");
        replaceString = replaceString.replaceAll("[^0-9.]", "");
        int[] atta = new int[replaceString.length()];
        for (int i = 0; i < replaceString.length(); i++)
            atta[i] = Character.getNumericValue(replaceString.charAt(i) - 1);
        return atta;
    }
}
