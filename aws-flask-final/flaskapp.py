from flask import Flask, render_template
app = Flask(__name__)

@app.route('/')
def hello_world():
  #return 'Welcome to Quick Shop!'
  return render_template('index.html')

@app.route('/policies')
def policies():
    return render_template('privacypolicy.htm')

@app.route('/download')
def download():
    return render_template('download.html')


if __name__ == '__main__':
  app.run(debug = True)
