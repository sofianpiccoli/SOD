#!/bin/bash

BASE_DIR="/Users/sofianietopiccoli/Documents/SOD"
LOG_DIR="$BASE_DIR/logs"

#!/bin/bash

BASE_DIR="/Users/sofianietopiccoli/Documents/SOD"
LOG_DIR="$BASE_DIR/logs"

mkdir -p "$LOG_DIR"

# Listas de proyectos y clases principales (ítems alineados por posición)
proyectos=(
    "SistemaCentral"
    "SensorTemperatura"
    "SensorRadiacion"
    "SensorLluvia"
    "SensorHumedad"
    "SensorHumedad2"
    "SensorHumedad3"
    "SensorHumedad4"
    "SensorHumedad5"
    "Electrovalvula"
    "Electrovalvula1"
    "Electrovalvula2"
    "Electrovalvula3"
    "Electrovalvula4"
    "Electrovalvula5"
)

clases=(
    "com.sistdist.sistemacentral.SistemaCentral"
    "com.sistdist.sensortemperatura.SensorTemperatura"
    "com.sistdist.sensorradiacion.SensorRadiacion"
    "com.sistdist.sensorlluvia.SensorLluvia"
    "com.sistdist.sensorhumedad.SensorHumedad"
    "com.sistdist.sensorhumedad2.SensorHumedad2"
    "com.sistdist.sensorhumedad3.SensorHumedad3"
    "com.sistdist.sensorhumedad4.SensorHumedad4"
    "com.sistdist.sensorhumedad5.SensorHumedad5"
    "com.sistdist.electrovalvula.Electrovalvula"
    "com.sistdist.electrovalvula1.Electrovalvula1"
    "com.sistdist.electrovalvula2.Electrovalvula2"
    "com.sistdist.electrovalvula3.Electrovalvula3"
    "com.sistdist.electrovalvula4.Electrovalvula4"
    "com.sistdist.electrovalvula5.Electrovalvula5"
)

# Función para compilar y ejecutar
ejecutar_proyecto() {
    local proyecto="$1"
    local clase="$2"
    local dir="$BASE_DIR/$proyecto"
    local log="$LOG_DIR/$proyecto.log"

    if [ ! -d "$dir" ]; then
        echo "No se encontró el directorio $dir, se salta."
        return
    fi

    cd "$dir" || { echo "No se pudo entrar a $dir"; return; }

    mvn clean package -q > /dev/null 2>&1

    if [ "$proyecto" == "SistemaCentral" ]; then
        java -cp target/*.jar "$clase" | tee "$log" &
    else
        java -cp target/*.jar "$clase" > "$log" 2>&1 &
    fi
}

# Ejecutar todos los proyectos
for i in "${!proyectos[@]}"; do
    ejecutar_proyecto "${proyectos[$i]}" "${clases[$i]}"
done

