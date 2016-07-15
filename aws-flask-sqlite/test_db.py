import sqlite3

# create or open a db called test.db
db = sqlite3.connect('test.db')

# get a cursor object
cursor = db.cursor()
cursor.execute("""DROP TABLE IF EXISTS test""")

cursor.execute("""CREATE TABLE test(
                  name text not null,
                  username text not null,
                  password text not null)
                  """)
db.commit()

# insert something
cursor = db.cursor()
name1 = 'Qian'
username1 = 'qx'
password1 = '123'

cursor.execute("""INSERT INTO test(name, username, password)
                  VALUES(?,?,?)""", (name1, username1, password1))

# another user -admin
name2 = 'Admin'
username2 = 'admin'
password2 = 'admin'

cursor.execute("""INSERT INTO test(name, username, password)
                  VALUES(?,?,?)""", (name2, username2, password2))
db.commit()
db.close()
