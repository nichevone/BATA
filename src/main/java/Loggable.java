public interface Loggable {
    char senderType = 'S';
    char receiverType = 'R';
    char handlerType = 'H';

    void log(char type, String message);
    void setLogger(InfoLogger logger);
}
