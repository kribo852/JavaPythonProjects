function makemap() {
	var rtn = {};
	for (var i = 255; i >= 0; i--) {
		rtn[String.fromCharCode(i)]=String.fromCharCode(getRandomInt(256));
		while(!rtn[String.fromCharCode(i)].match(/^[0-9A-Za-z]{1}$/)) {
			rtn[String.fromCharCode(i)]=String.fromCharCode(getRandomInt(256));
		}
	}
	rtn[' ']=' ';
	return rtn;
}

function getRandomInt(max) {
  return Math.floor(Math.random() * Math.floor(max));
}

function clearTextPhrase() {
	var sentence = Math.random();
	if(sentence<0.33){
		return 'KEEP IT LOUD ÄR AWESOME!! MATTECENTRUM GER ALLA SOM FRÅGAR EN TABLETTASK. ANALYS AV FREKVENSER FÖR BOKSTÄVER, BOKSTAVSFÖLJDER OCH ORD ÄR ANVÄNDBARA FÖR ATT LÖSA ENKLA CHIFFER. JU LÄNGRE EN CHIFFER-TEXT ÄR DESTO MER ANVÄNDBAR INFORMATION FÖR ATT LÖSA CHIFFRET INNEHÅLLER DEN.';
	}
	if(sentence<0.66){
		return 'MATTECENTRUM ÄR EN RIKSTÄCKANDE DEMOKRATISK, POLITISKT OCH RELIGÖST OBUNDEN ORGANISATION SOM ANORDNAR INDIVIDUELL LÄXHJÄLP. BLI MEDLEM OCH FÅ MÖJLIGHET ATT PÅVERKA MATTECENTRUMS UTVECKLINGSARBETE. VARJE ÅR HAR MATTECENTRUM ÅRSMÖTE TILL VILKET ALLA MEDLEMMAR ÄR INBJUDNA. ALLA SOM VILL KAN BLI MEDLEMMAR, MER INFORMATION FINNS PÅ VÅR HEMSIDA';
	}
	return 'UTÖVER FREKVENSER FÖR BOKSTÄVER, BIGRAM OCH TRIGRAM, ALLTSÅ BOKSTAVSFÖLJDER MED 2 ELLER 3 BOKSTÄVER, KAN KONEXT ANVÄNDAS FÖR ATT LÖSA KRYPTON. TILL EXEMPEL ÄR ORDET MATTECENTRUM VANLIGT FÖREKOMMANDE I DE HÄR KRYPTONA. GENOM ATT GISSA ATT ETT ORD ÄR MATTECENTRUM FÅS EN STOR MÄNGD INFORMATION OM VAD DE ANDRA ORDEN KAN VARA'  
}

function isDecrypted(string1, string2){
	return string1.toLowerCase()=== string2.toLowerCase();
}

function replace(lettermapping, correctletter, mappedletter){
	var newlettermapping = Object.create(lettermapping);
	newlettermapping[correctletter] = mappedletter;
	return newlettermapping;
}
