find com -name "*.class" -delete

find com -name "*.java" > sources.txt

javac @sources.txt

java com.compilers.lox.lox input.txt

find com -name "*.class" -delete