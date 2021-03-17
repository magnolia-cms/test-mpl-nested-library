if(sh(script: 'test -f pom.xml', returnStatus: true) == 0)
  MPLModule('Maven Build', CFG)
else if (sh(script: 'test -f package.json', returnStatus: true) == 0)
  MPLModule('Node Build', CFG)
