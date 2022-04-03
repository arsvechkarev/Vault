package com.arsvechkarev.vault.common

import buisnesslogic.FileSaver

class TestFileSaver : FileSaver {

    private var text: String = ""

    override fun saveData(data: String) {
        this.text = data
    }

    override fun readData(): String {
        return text
    }

    override fun delete() {
        text = ""
    }
}