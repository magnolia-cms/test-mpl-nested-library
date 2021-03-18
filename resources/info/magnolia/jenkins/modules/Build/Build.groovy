if(fileExists('pom.xml'))
  MPLModule('Maven Build', CFG)
else if (fileExists('package.json'))
  MPLModule('Node Build', CFG)
