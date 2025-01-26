import requests
from flask import Blueprint, render_template, jsonify
import requests

router = Blueprint('router', __name__)

@router.route('/')
def home():
    return render_template('template.html')


@router.route('/mapagrafos')
def mapagrafos():
    url = "http://localhost:8099/api/parques/mapadegrafos"
    
    try:
        r = requests.get(url)
        if r.status_code == 200:
            graph_data = r.json()
            return render_template('grafos/grafos.html', graph_data=graph_data)
        else:
            print("Error al obtener grafo: ", r.text)
            return jsonify({"error": "No se pudo obtener el grafo"}), r.status_code

    except requests.exceptions.RequestException as e:
        print("Error de conexión: ", str(e))
        return jsonify({"error": "No se pudo conectar con el servidor"}), 500
