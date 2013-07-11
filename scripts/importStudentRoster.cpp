#include <math.h>
#include <iostream>
#include <string>
#include <mysql++.h>
#include <fstream>

using namespace std;
using namespace mysqlpp;

int main(int argc, char* argv[])
{
        Connection c("magpie", "localhost", "stephen",
                        "iloverae", 0);
        ifstream ifs;
        ifs.open(argv[1]);
        string junk, email, firstName, lastName, userName;

        int sectionNum = argv[1][3] - 48;

        Query q = c.query();
        Query r = c.query();
 	Query s = c.query();
 	Query t = c.query();
 	while (ifs.peek() != EOF)
	{
            int uid;
        	try
        	{
        		getline (ifs, junk, ':');
        		getline (ifs, email, '"');
        		userName = email;
        		userName.erase(userName.find_first_of('@'));
        		getline (ifs, junk, '"');
        		getline (ifs, firstName, ' ');
        		getline (ifs, lastName, '"');
        		getline (ifs, junk);
        		if (lastName[2] == ' ') {
                    lastName.erase(0,3);
                }
                if (lastName[1] == '\'') {
                    lastName.erase(1,1);
                }
                //
                // Check and see whether user already exists.
                s << "SELECT * FROM user WHERE username='" << userName << "'" << endl;
                StoreQueryResult uqr = s.store();
                if (uqr.num_rows() <= 0) {
        		    cout << "Creating student account for " << email  << " " << userName << " " << firstName << " " << lastName << endl;
                    q << "INSERT INTO user (username,password,firstname,lastname,role) VALUES ('" << userName << "', SHA1('" << userName << "'), '" << firstName << "','" << lastName << "','student')" << endl;
                    q.execute();

                    r << "SELECT last_insert_id()" << endl;
                    StoreQueryResult lid = r.store();
                    uid = lid[0][0];
                }
                else {
                    cout << "(user " << userName << " already in database.)" << endl;
                    uid = uqr[0]["uid"];
                }

                t << "INSERT INTO courseLink (uid,courseid) VALUES (" << uid << ", " << sectionNum << ")" << endl;
                t.execute();
        		
        		q.reset();
        		r.reset();
        		s.reset();
        		t.reset();
        		
                }
                catch (const Exception &e)
                {
                      cout << "I just caught error: " << e.what() << endl;
                      exit(1);
        	}
	}
	c.disconnect();
	return 0;
}
