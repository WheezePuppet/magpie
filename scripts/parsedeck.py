import sys
import _mysql

if len(sys.argv) < 3:
	print "USAGE: python parsedeck.py [file to parse] [courseid]."
	sys.exit(1);

infile = sys.argv[1]
courseid = sys.argv[2]
INPUT = open(infile)
deck = INPUT.readline().strip()
fulltext = INPUT.read()

db=_mysql.connect(host="localhost",user="stephen",passwd="iloverae",db="magpie",unix_socket="/var/run/mysqld/mysqld.sock")
db.query("INSERT INTO deck (deckname, courseid, active) VALUES ('" + deck + "', '" + courseid + "', '1')")
did = db.insert_id()

cards = fulltext.split("\n.\n")
for card in cards:
	qa = card.split("\n,\n")
	question, answer = qa[0].strip(), qa[1].strip()
	db.query("INSERT INTO card (question, answer, did) VALUES ('" + db.escape_string(question) + "', '" + db.escape_string(answer) + "', '" + str(did) + "')")
	cid = db.insert_id()
	db.query("INSERT INTO card (question, answer, did, inverseid) VALUES ('" + db.escape_string(answer) + "', '" + db.escape_string(question) + "', '" + str(did) + "', '" + str(cid) + "')")
	inverseid = db.insert_id()
	db.query("UPDATE card SET inverseid='" + str(inverseid) + "' WHERE cid='" + str(cid) + "'")
