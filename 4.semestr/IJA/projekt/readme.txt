Název projektu: UMLApplication
Členové týmu: Hájek Vojtěch (xhajek51)
	      Polívka Tomáš (xpoliv06)

1) Pro spuštění aplikace je potřeba instalace maven:
		sudo apt install maven

2) Pro přeložení aplikace musíte být ve složce, kde se nachází soubor pom.xml a zadejte příkaz:
		mvn package

3) Pro spuštění aplikace jděte do složky dest a zadejte příkaz:
		java -jar ija-app.jar

 a) Pro nahrání ukázkového diagramu jsme ke složce připojili soubor test.json, po spuštění aplikace je potřeba zadat jeho celou cestu.
	V textovém režimu zadáte číslo 2 pro nahrání diagramu a číslo 5 pro jeho vykreslení.
	V grafickém režimu klikněte na tlačitko upload a po zadání cesty potvrďte kliknutím na tlačitko submit.

4) Pro vygenerování dokumentace musíte být opět ve složce, kde se nachází soubor pom.xml a zadejte příkaz:
		mvn javadoc:javadoc