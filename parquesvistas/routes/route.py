import requests
from flask import Blueprint, render_template, jsonify
import requests

router = Blueprint('router', __name__)

@router.route('/')
def home():
    return render_template('template.html')


@router.route('/mapagrafos')
def mapagrafos():
    r = requests.get("http://localhost:8099/api/parques/mapadegrafos")
    
    try:
       if r.status_code == 200:
            graph_data = r.json()
            return render_template('grafos/grafos.html', graph_data=graph_data)
       else:
            print("Error al obtener grafo: ", r.text)
            return jsonify({"error": "No se pudo obtener el grafo"}), r.status_code

    except requests.exceptions.RequestException as e:
        print("Error de conexión: ", str(e))
        return jsonify({"error": "No se pudo conectar con el servidor"}), 500

@router.route('/adyacencias')
def adyacenciasaleatorias():
    r = requests.get("http://localhost:8099/api/parques/misgrafos")
    print(r.status_code)  # Imprime el código de estado HTTP
    print(r.text)         # Imprime el contenido de la respuesta

    data = r.json()
    ## imprimirn en consola
    print(data)
    return render_template('grafos/adyacencias.html', lista=data)

@router.route('/nueva_adyacencia', methods=['GET'])
def nueva_adyacencia():
    # Hacer una solicitud GET al backend de Java
    r = requests.get("http://localhost:8099/api/parques/adyacencias_aleatorias")

    try:
        if r.status_code == 200:
            # Obtener la respuesta de la API y pasar los datos al template
            data = r.json()
            return jsonify(data)  # Retornar la respuesta como JSON para procesar en el frontend
        else:
            return jsonify({"error": "No se pudo obtener la adyacencia"}), r.status_code

    except requests.exceptions.RequestException as e:
        print("Error de conexión: ", str(e))
        return jsonify({"error": "No se pudo conectar con el servidor"}), 500
    
@router.route('/calculo_camino_corto/<int:origen>/<int:destino>/<int:algoritmo>', methods=['GET'])
def calcular_camino_corto(origen, destino, algoritmo):
    try:
        # Realizar la solicitud al backend de Java (API REST)
        url = f"http://localhost:8099/api/parques/camino_corto/{origen}/{destino}/{algoritmo}"
        r = requests.get(url)

        if r.status_code == 200:
            # Obtener el resultado de la API
            data = r.json()
            return jsonify(data)
        else:
            return jsonify({"error": "No se pudo calcular el camino corto"}), r.status_code

    except requests.exceptions.RequestException as e:
        print("Error de conexión:", str(e))
        return jsonify({"error": "No se pudo conectar con el servidor de cálculo"}), 500
    
@router.route('/camino', methods=['GET'])
def mostrar_formulario():
    r = requests.get("http://localhost:8099/api/parques/misgrafos")
    data = r.json()
    return render_template('grafos/camino.html', data= data)  # Nombre del archivo de tu template

@router.route('/bfs/<int:origen>', methods=['GET'])
def ejecutar_bfs(origen):
    try:
        # Realizar la solicitud al backend de Java para ejecutar BFS
        url = f"http://localhost:8099/api/parques/bfs/{origen}"
        r = requests.get(url)

        if r.status_code == 200:
            # Verificar si la respuesta tiene contenido
            if r.text.strip():
                # Intentar convertir la respuesta a JSON
                data = r.json()
                return jsonify(data)  # Retornar la respuesta como JSON para procesar en el frontend
            else:
                return jsonify({"error": "La respuesta está vacía"}), 500
        else:
            return jsonify({"error": f"Error al ejecutar BFS. Código de estado: {r.status_code}"}), r.status_code

    except requests.exceptions.RequestException as e:
        print("Error de conexión:", str(e))
        return jsonify({"error": "No se pudo conectar con el servidor de BFS"}), 500



