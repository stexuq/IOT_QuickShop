import csv
import sqlite3
from flask import Flask, request, g, jsonify, render_template
app = Flask(__name__)

DATABASE = '/var/www/html/flaskapp/test2.db'

app.config.from_object(__name__)

def connect_to_database():
    return sqlite3.connect(app.config['DATABASE'])

def get_db():
    db = getattr(g, 'db', None)
    if db is None:
        db = g.db = connect_to_database()
    return db

@app.before_request
def before_request():
    g.db = sqlite3.connect("test2.db")

@app.teardown_appcontext
def close_connection(exception):
    db = getattr(g, 'db', None)
    if db is not None:
        db.close()

def execute_query(query, args=()):
    cur = get_db().execute(query, args)
    rows = cur.fetchall()
    cur.close()
    return rows

def execute_insert(query, args=()):
    cur = get_db().cursor()
    cur.execute(query, args)
    get_db().commit()

@app.route('/')
def hello_world(name='Flask'):
  #return 'Hello from {}!'.format(name)
  return render_template("index.html")

@app.route('/countme/<input_str>')
def count_me(input_str):
  return input_str

@app.route("/viewdb")
def viewdb():
    rows = execute_query("""SELECT * FROM test""")
    return '<br>'.join(str(row) for row in rows)

@app.route('/viewdbjson')
def viewdb_json():
    rows =  execute_query("""SELECT * FROM test""")
    l = []
    for row in rows:
        l.append({'name':row[0], 'username':row[1], 'age':row[2], 'password':row[3]})

    return jsonify(results = l)
    #return render_template('json.html', rows=rows)

@app.route('/test', methods=['GET', 'POST'])
def test():
    data = request.get_json(force=True)
    if data:
        # insert into sqlite database
        name = data['name']
        username = data['username']
        password = data['password']
        #age = int(data['age'])
        age = None
        con = get_db()
        cur = con.cursor()
        cur.execute("""INSERT INTO test(name, username, age, password)
                  VALUES(?,?,?,?)""", (name, username, age, password))
        con.commit()
        return 'inserted'
        
        #return render_template('test.html', data)
    else:
        return 'no json data received'

@app.route('/insert')
def insert():
    name = 'test'
    username = 'test'
    password = 'test'
    con = get_db()
    if con is None:
        return 'connection is None'
    else:
        cur = con.cursor()
        #cur.execute("""INSERT INTO test(name, username, password)
        #       VALUES(?,?,?)""", (name, username, password))
        #con.commit()
        #return 'inserted'
        cur.execute("SELECT * FROM test WHERE username = ?", ('qx',))
        #cur.execute("""DELETE FROM test WHERE username=?""", ('admin',))
        rows = cur.fetchall()
        return '<br>'.join(str(row) for row in rows)
   

@app.route('/post', methods=['POST'])
def post():
    data = request.get_json(force=True)
    if data:
        return str(data)
    else:
        return 'no'
    #l = [data['name'], data['username'], data['age'], data['password']]
    #print(data)
    #return jsonify(data)
    #return render_template('test.html', l)

@app.route('/register/<name>/<username>/age/<password>')
def register():
    rows = execute_query("""SELECT * FROM natlpark""")
    #return str(rows[0][0])
    return render_template('json.html', rows=rows)


if __name__ == '__main__':
  app.run(debug=True)
