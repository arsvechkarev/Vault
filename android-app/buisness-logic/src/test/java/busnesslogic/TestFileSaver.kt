package busnesslogic

import buisnesslogic.FileSaver

class TestFileSaver : FileSaver {

    private var data: ByteArray? = null

    override fun saveData(data: ByteArray) {
        this.data = data
    }

    override fun readData(): ByteArray? {
        return data
    }

    override fun delete() {
        data = null
    }
}