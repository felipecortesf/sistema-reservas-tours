const apiKey = 'd013045cd776cf4de51d1b2348038c84';
const token = 'ATTA4e96b26da15515581102c8c6a9f12f4b2662c2757589e49e0393544dd3fa0c8fC9D68D78';

const planSemanas = [
  {
    name: '📅 Semana 1 - Preparación',
    cards: [
      '[Felipe] Redactar borrador de mail de regalo',
      '[Felipe] Optimizar SEO interno de Bandcamp (tags/descripciones)',
      '[Gabo] Recopilar tracks de Devil Kids 3, Loco Blue y Devil Split',
      '[Gabo] Definir orden y categorías para SoundCloud',
      '[Max] Definir concepto visual y línea estética de los 3 singles'
    ]
  },
  {
    name: '📅 Semana 2 - Producción y Contactos',
    cards: [
      '[Felipe] Preparar base de datos en plataforma de email marketing',
      '[Felipe] Revisar herramientas de promoción interna en Bandcamp',
      '[Gabo] Contactar a Artro (Costa Rica), amigo de Brasil y Devil Kids antiguos',
      '[Max] Diseñar portada de Devil Kids 3',
      '[Max] Crear banners para SoundCloud y Bandcamp'
    ]
  },
  {
    name: '📅 Semana 3 - Lanzamiento SC y Email 1',
    cards: [
      '[Felipe] ENVIAR EMAIL DE REGALO a seguidores (Track exclusivo)',
      '[Gabo] Subir tracks y armar listas por categoría en SoundCloud',
      '[Max] Diseñar portada de Loco Blue'
    ]
  },
  {
    name: '📅 Semana 4 - Foco Bandcamp y Single 1',
    cards: [
      '[Felipe] Activar promos internas en Bandcamp',
      '[Gabo] LANZAMIENTO SINGLE 1: Devil Kids 3',
      '[Max] Diseñar portada de Devil Split'
    ]
  },
  {
    name: '📅 Semana 5 - Single 2 y Redes',
    cards: [
      '[Felipe] Enviar Email 2 (Detrás de escenas + Tráfico)',
      '[Gabo] LANZAMIENTO SINGLE 2: Loco Blue',
      '[Max] Crear clips de video cortos (Reels/Stories)'
    ]
  },
  {
    name: '📅 Semana 6 - Single 3 y Cierre',
    cards: [
      '[Felipe] Analizar métricas de correos y ventas en Bandcamp',
      '[Gabo] LANZAMIENTO SINGLE 3: Devil Split',
      '[Gabo] Consolidar feedback de Artro y contacto de Brasil',
      '[Max] Archivar y ordenar catálogo visual final'
    ]
  },
  {
    name: '🎉 ¡HECHO / DONE!',
    cards: []
  }
];

async function apiTrello(endpoint, method = 'POST', params = {}) {
  const urlParams = new URLSearchParams({ key: apiKey, token: token, ...params });
  const response = await fetch(`https://api.trello.com/1${endpoint}?${urlParams}`, { method });
  if (!response.ok) {
    const text = await response.text();
    throw new Error(`Error HTTP ${response.status}: ${text}`);
  }
  return response.json();
}

async function crearTableroMusical() {
  try {
    console.log('Creando el tablero en Trello...');
    const board = await apiTrello('/boards/', 'POST', {
      name: 'Plan de Lanzamiento Musical - 6 Semanas',
      defaultLists: 'false'
    });

    console.log(`✅ Tablero creado con éxito. ID: ${board.id}`);

    for (const semana of planSemanas) {
      console.log(`Creando lista: ${semana.name}`);
      const list = await apiTrello('/lists', 'POST', {
        name: semana.name,
        idBoard: board.id
      });
      
      for (const cardName of semana.cards) {
        console.log(`  -> Añadiendo tarjeta: ${cardName}`);
        await apiTrello('/cards', 'POST', {
          name: cardName,
          idList: list.id
        });
      }
    }

    console.log('\n🚀 ¡Todo listo! Ya puedes revisar tu Trello.');
  } catch (error) {
    console.error('❌ Hubo un error al crear el tablero:', error.message);
  }
}

crearTableroMusical();
