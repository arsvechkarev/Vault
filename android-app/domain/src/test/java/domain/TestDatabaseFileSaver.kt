package domain

class TestDatabaseFileSaver : DatabaseFileSaver {
  
  private var data: ByteArray? = null
  
  override fun doesDatabaseExist(): Boolean {
    return data != null
  }
  
  override suspend fun save(database: ByteArray) {
    this.data = database
  }
  
  override suspend fun read(): ByteArray? {
    return data
  }
  
  override suspend fun delete() {
    data = null
  }
  
  override suspend fun getFileUri(): String {
    return "myfile/$data"
  }
}
