package monads

case class SafeValue[+T](private val internalValue: T) {

  def get: T = synchronized {
    internalValue
  }

  def flatMap[S](transformer: T => SafeValue[S]): SafeValue[S] =
    synchronized {
      transformer(internalValue)
    }
}
