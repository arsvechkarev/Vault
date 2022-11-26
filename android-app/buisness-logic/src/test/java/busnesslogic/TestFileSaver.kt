package busnesslogic

import buisnesslogic.FileSaver

class TestFileSaver : FileSaver {
  
  private var data: ByteArray? = null
  
  override suspend fun saveData(data: ByteArray) {
    this.data = data
  }
  
  override suspend fun readData(): ByteArray? {
    return data
  }
  
  override suspend fun delete() {
    data = null
  }
  
  override suspend fun getFileUri(): String {
    return "myfile/$data"
  }
}
