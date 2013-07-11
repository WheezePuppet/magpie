import sys
import _mysql

db=_mysql.connect(host="localhost",user="stephen",passwd="iloverae",db="magpie",unix_socket="/var/run/mysqld/mysqld.sock")

db.query("select question from card")

r = db.store_result()

print "hi"
print r.fetch_row(maxrows=0)
