import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset
import org.mozilla.universalchardet.UniversalDetector

fun detectCharset(file: File): Charset {
    val buf = ByteArray(1024)
    val detector = UniversalDetector(null)

    FileInputStream(file).use { fis ->
        var nread: Int
        while (fis.read(buf).also { nread = it } > 0 && !detector.isDone) {
            detector.handleData(buf, 0, nread)
        }
        detector.dataEnd()
    }
    val encoding = detector.detectedCharset ?: "UTF-8"
    return Charset.forName(encoding)
}

fun main() {
    val filePath = readln()
    val file = File(filePath)

    val charset = detectCharset(file)
    println("Кодировка: $charset")

    val text = file.readText(charset)
    println("Текст из файла:\n$text")

    val win1251Charset = Charset.forName("Windows-1251")
    file.writeText(text, win1251Charset)

    if (detectCharset(file) == win1251Charset) println("Файл сохранен в кодировке Windows-1251.")
    else println("Ошибка! Файл не был преобразован")
}