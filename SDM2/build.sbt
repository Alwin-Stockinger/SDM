lazy val commonSettings = Seq(
  organization := "at.ac.univie",
  version := "0.1.0",
  scalaVersion := "2.10.5"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "mysparkapp"
  )

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "2.1.0",
  "org.apache.spark" % "spark-mllib_2.10" % "2.1.0"
)

/*** if you use assembly.sbt to build a fat jar, then use the provided instead 
libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "2.1.0" % provided,
  "org.apache.spark" % "spark-mllib_2.10" % "2.1.0" % provided
)
*/
