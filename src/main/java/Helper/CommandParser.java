package Helper;

class CommandParser {

    static int[] validateAttack(String command) {
        command = command.substring(command.indexOf(" ") + 1);
        String[] splitedPositions = command.split(" ");
        int[] attackPositions = new int[splitedPositions.length];
        for (int i = 0 ; i < splitedPositions.length ; i++)
             attackPositions[i] = Integer.parseInt(splitedPositions[i]) - 1;
        return attackPositions;
    }
}
