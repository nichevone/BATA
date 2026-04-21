public interface Loggable {
    char senderType = 'S';
    char receiverType = 'R';

    void log(char type, String message);
    void setLogger(InfoLogger logger);
}
