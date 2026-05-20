#!/bin/bash
# Colores para la salida
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

MICROSERVICES=("ms-usuarios" "ms-catalogo-tours" "ms-reservas" "ms-notificaciones" "ms-embarques" "ms-comunicacion-agencia" "ms-reportes" "ms-whatsapp" "ms-notificaciones-push" "ms-pagos")

echo "============================================="
echo "  Iniciando compilación de Microservicios    "
echo "============================================="

for MS in "${MICROSERVICES[@]}"; do
    if [ -d "$HOME/Documents/sistema-reservas-tours/$MS" ]; then
        echo -e "\n📦 Compilando ${GREEN}$MS${NC}..."
        cd "$HOME/Documents/sistema-reservas-tours/$MS"
        
        # Ejecuta clean compile usando el wrapper local
        if ./mvnw clean compile -DskipTests > /dev/null 2>&1; then
            echo -e "✅ ${GREEN}$MS compilado con éxito${NC}"
        else
            echo -e "❌ ${RED}Error de compilación en $MS${NC}. Revisa los logs ejecutando: cd $MS && ./mvnw compile"
        fi
    else
        echo -e "⚠️  No se encontró el directorio para $MS"
    fi
done
