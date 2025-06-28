# (optional but clean) delete old .class files
find com -name "*.class" -delete

# recompile all Java source files
find com -name "*.java" > sources.txt

javac @sources.txt

java com.compilers.lox.lox input.txt

# for helper tools 

java com.compilers.tool.GenerateAst ./com/compilers/lox


javac com/compilers/tool/GenerateAst.java
