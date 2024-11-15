//
def f(x: Int): Int = x * 2

def g(x: Int): Int = x + 2

def h(x: Int): Int = f(g(x))

val input = 4
println(s"g($input) = ${g(input)}")
println(s"g(f($input)) = ${g(f(input))}")
println(s"h($input) = ${h(input)}")



// Staticly typed example
val name: String = "Nathanael"
val age: Int = 20

// Operators
val sum = 1 + 2
val sub = 1 - 2
val mul = 1 * 2
val div = 1 / 2
val mod = 10 % 5

val x = 5
val y = 10

// Call of the + method
val z = x.+(y)


// Trait
trait Shape {
  def area(): Int
}

class Circle(radius: Int) extends Shape {
   def area(): Int = (3.14 * radius * radius).toInt
}

class Rectangle(length: Int, width: Int) extends Shape {
   def area(): Int = length * width
}

val circle = new Circle(2)
val rectangle = new Rectangle(2, 3)

println(s"Area of circle: ${circle.area()}")
println(s"Area of rectangle: ${rectangle.area()}")


// Tuples

val tuple = (1, "Hello", 2.4)
val second = tuple._2

//Collections
val array = Array(1, 2, 3, 4, 5)
array(0) = 10

val list = List(1, 2, 3, 4, 5)
val set = Set(1, 2, 3, 4, 5)


val listTo = (1 to 5).toList


set.contains(1)
set.contains(10)




val map = Map("France" -> "Paris", "USA" -> "Washington DC", "Poland" -> "Warsaw")
val capitalOfFrance = map("France")
val map2 = map + ("Germany" -> "Berlin")
val capitalOfGermany = map2("Germany")
val capitalOfUSA = map2("USA")


val list2 = list.map(x => x * 2)
val list2Reduced = list.map(_ * 2)


val line = "Scala is a programming language"
val SingleSpace = " "
val words = line.split(SingleSpace)
val arrayOfListOfChar = words.map(_.toList)

val arrayOfChat = words.flatMap(_.toList)


val list3 = (1 to 10).toList
val listFiltered = list3.filter (_ % 2 == 0)

words.foreach(println)

val sumOfList = list3.reduce(_ + _)
val productOfList = list3.reduce(_ * _)