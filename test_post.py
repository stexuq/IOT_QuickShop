import requests

url = "http://ec2-54-208-116-244.compute-1.amazonaws.com/test"
data = {'name': 'test', 'username': 'test', 'age': '22', 'password': '123'}
#headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
r = requests.post(url, json=data)

print(r.status_code)
print(r.text)