import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.readText
import kotlin.io.path.writeText

fun generateFilesForDay(day: String) {

    Path("../inputs/Day$day.txt").createFile()
    Path("../inputs/Day${day}_test.txt").createFile()
    val template = Path("DayTemplate.kt").readText()
    val fileContents = template.replace("%DAY%", day)
    Path("Day${day}.kt").createFile().writeText(fileContents)
}

Paths.get("").toAbsolutePath().println()
generateFilesForDay("11")