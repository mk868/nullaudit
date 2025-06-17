package eu.softpol.lib.nullaudit.maven.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessageSolver {

  private final ResourceBundle bundle;

  public MessageSolver() {
    this.bundle = ResourceBundle.getBundle("eu.softpol.lib.nullaudit.maven.messages");
  }

  public String resolve(MessageKey key, Object... args) {
    if (args.length != key.argCount()) {
      throw new IllegalArgumentException(String.format(
          "MessageKey '%s' expects %d args, got %d",
          key, key.argCount(), args.length));
    }
    String pattern = bundle.getString(key.key());
    return key.argCount() == 0
        ? pattern
        : MessageFormat.format(pattern, args);
  }

}
