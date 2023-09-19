package buisnesslogic

import buisnesslogic.model.PasswordCharacteristic
import java.util.EnumSet

interface PasswordCharacteristicsProvider {
  
  fun getCharacteristics(password: Password): EnumSet<PasswordCharacteristic>
}
