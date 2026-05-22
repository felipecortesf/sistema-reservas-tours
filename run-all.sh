#!/bin/sh
echo "============================================="
echo "   Iniciando Ecosistema de Microservicios    "
echo "============================================="

BASE_DIR=~/Documents/sistema-reservas-tours

# Lista simple de servicios separados por espacios para máxima compatibilidad
SERVICIOS="ms-usuarios ms-catalogo-tours ms-reservas ms-notificaciones ms-embarques ms-comunicacion-agencia ms-reportes ms-whatsapp ms-notificaciones-push ms-pagos"

for SERVICIO in $SERVICIOS; do
  echo "🚀 Levantando $SERVICIO..."
  
  cd "$BASE_DIR/$SERVICIO" 2>/dev/null
  if [ $? -eq 0 ]; then
    # Ejecuta en segundo plano redirigiendo logs
    ./mvnw spring-boot:run > "$BASE_DIR/${SERVICIO}.log" 2>&1 &
    sleep 2 # Evita saturar la CPU al arrancar en ráfaga
  else
    echo "❌ No se encontró el directorio para $SERVICIO"
  fi
done

echo "============================================="
echo " Todos los servicios fueron lanzados en BG.  "
echo " Monitorea los logs con: tail -f *.log       "
echo "============================================="
