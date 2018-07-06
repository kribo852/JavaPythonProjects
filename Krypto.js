

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

/*function activeLetters(){
	return ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","Å","Ä","Ö","!",".",""]; 
}*/

clearTextPhrase = () => 'KEEP IT LOUD ÄR AWESOME!! MATTECENTRUM GER ALLA SOM FRÅGAR EN TABLETTASK. ANALYS AV BOKSTÄVER, BOKSTAVSFÖLJDER OCH ORD ÄR ANVÄNDBARA FÖR ATT LÖSA ENKLA CHIFFER. JU LÄNGRE EN CHIFFER-TEXT ÄR DESTO MER ANVÄNDBAR INFORMATION INNEHÅLLER DEN';

function isDecrypted(string1, string2){
	return string1.toLowerCase()=== string2.toLowerCase();
}

function replace(lettermapping, correctletter, mappedletter){

	var newlettermapping = Object.create(lettermapping);
	newlettermapping[correctletter] = mappedletter;
	return newlettermapping;

}

function mapFrequentLetters(lettermapping, cleartextphrase){
	frequencies = ["E", "A", "N", "R", "T"];

	letters = cleartextphrase.match(/.{1,1}/g);

	

	letters.forEach(letter => {
		if(frequencies.indexOf(letter)!=-1) {

		}
	});


}