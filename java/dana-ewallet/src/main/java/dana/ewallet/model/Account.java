package dana.ewallet.model;

public class Account {
  private final int id;
  private final String name;

  public Account(int id, String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Nama wajib diisi");
    }
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}