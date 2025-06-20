package eu.softpol.lib.nullaudit.core.analyzer;

import org.jspecify.annotations.Nullable;

public interface CodeLocation {

  @Nullable
  String module();

  String packageName();

  record PackageLocation(
      @Nullable String module,
      String packageName
  ) implements CodeLocation {

    public ClassLocation classLocation(String className) {
      return new ClassLocation(module, packageName, className);
    }
  }

  record ClassLocation(
      @Nullable String module,
      String packageName,
      String className
  ) implements CodeLocation {

    public MemberLocation memberLocation(String memberName) {
      return new MemberLocation(module, packageName, className, memberName);
    }

    public PackageLocation packageLocation() {
      return new PackageLocation(module, packageName);
    }
  }

  record MemberLocation(
      @Nullable String module,
      String packageName,
      String className,
      String memberName
  ) implements CodeLocation {

    public ClassLocation classLocation() {
      return new ClassLocation(module, packageName, className);
    }
  }
}
