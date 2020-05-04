fun main() {
    val apair = Pair("one","two")
    val alsoapair = "one" to "two"
    val apairofobjects :Pair<Object,Object> = Pair(Object(),Object())

    println("pairs!")
    println("A pair: $apair")
    println("Also a pair: $alsoapair")
    println("Thats a pair of objects: $apairofobjects")
}