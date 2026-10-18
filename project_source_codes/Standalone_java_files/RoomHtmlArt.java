package com.my.cardbattle;
import java.util.*;

//AI generated map visualizer
public class RoomHtmlArt {
	
	static final int NODE_W = 130;
	static final int NODE_H = 50;
	static final int H_GAP  = 50;
	static final int V_GAP  = 110;
	static final int MARGIN = 40;
	
	public static String mapToHtml(List<Map<String, Object>> roomsList) {
		return mapToHtml(roomsList, null, null, null);
	}
	
	public static String mapToHtml(List<Map<String, Object>> roomsList, String rootId) {
		return mapToHtml(roomsList, rootId, null, null);
	}
	
	public static String mapToHtml(List<Map<String, Object>> roomsList,
	String rootId,
	String currentRoomId,
	String currentRoomState) {
		Map<String, Room> rooms = buildGraph(roomsList);
		SortedMap<Integer, List<Room>> levels = byLevel(rooms, rootId);
		
		Map<String, int[]> pos = new HashMap<>();
		int maxX = 0, maxY = 0;
		
		int row = 0;
		for (Map.Entry<Integer, List<Room>> e : levels.entrySet()) {
			int y = MARGIN + row * (NODE_H + V_GAP);
			List<Room> rowRooms = e.getValue();
			for (int i = 0; i < rowRooms.size(); i++) {
				int x = MARGIN + i * (NODE_W + H_GAP);
				pos.put(rowRooms.get(i).id, new int[]{x, y});
				maxX = Math.max(maxX, x + NODE_W);
				maxY = Math.max(maxY, y + NODE_H);
			}
			row++;
		}
		
		int width  = maxX + MARGIN;
		int height = maxY + MARGIN;
		
		
		StringBuilder svg = new StringBuilder();
		svg.append("<svg xmlns='http://www.w3.org/2000/svg' width='").append(width)
		.append("' height='").append(height).append("' viewBox='0 0 ")
		.append(width).append(' ').append(height).append("' preserveAspectRatio='xMidYMid slice'>\n");;
		
		// Arrowhead marker
		svg.append("<defs><marker id='arrow' viewBox='0 0 10 10' refX='9' refY='5' ")
		.append("markerWidth='6' markerHeight='6' orient='auto-start-reverse'>")
		.append("<path d='M0,0 L10,5 L0,10 z' fill='#555'/></marker></defs>\n");
		
		// Edges
		for (Room r : rooms.values()) {
			int[] p1 = pos.get(r.id);
			if (p1 == null) continue;
			double x1 = p1[0] + NODE_W / 2.0, y1 = p1[1] + NODE_H;
			for (String nid : r.nextIds) {
				int[] p2 = pos.get(nid);
				if (p2 == null) continue;
				double x2 = p2[0] + NODE_W / 2.0, y2 = p2[1];
				svg.append("<line x1='").append(x1).append("' y1='").append(y1)
				.append("' x2='").append(x2).append("' y2='").append(y2)
				.append("' stroke='#555' stroke-width='1.5' ")
				.append("marker-end='url(#arrow)'/>\n");
			}
		}
		
		// Nodes
		final String CURRENT_FILL_NORMAL   = "#ffe9a8";   // warm highlight
		final String CURRENT_FILL_COMPLETED   = "#a8ffa8";   // light green highlight
		final String CURRENT_STROKE = "#c77800";
		final String NORMAL_FILL    = "#e6f0ff";
		final String NORMAL_STROKE  = "#3c5a96";
		
		for (Room r : rooms.values()) {
			int[] p = pos.get(r.id);
			if (p == null) continue;
			
			boolean isCurrent = currentRoomId != null && currentRoomId.equals(r.id);
			
			// Notice: js could overwrite the colourings defined here, need to edit those too
			String fill   = isCurrent ? ("completed".equals(currentRoomState)? CURRENT_FILL_COMPLETED: CURRENT_FILL_NORMAL)   : NORMAL_FILL;
			String stroke = isCurrent ? CURRENT_STROKE : NORMAL_STROKE;
			int    sw     = isCurrent ? 3 : 1;         // thicker border for current
			
			// Add data-id to each rect so JS can find it:
			svg.append("<rect class='node' data-id='").append(escape(r.id))
			.append("' x='").append(p[0]).append("' y='").append(p[1])
			.append("' width='").append(NODE_W).append("' height='").append(NODE_H)
			.append("' rx='6' ry='6' fill='").append(fill)
			.append("' stroke='").append(stroke)
			.append("' stroke-width='").append(sw).append("'/>\n");
			
			svg.append("<text x='").append(p[0] + NODE_W / 2).append("' y='")
			.append(p[1] + NODE_H / 2 + 5)
			.append("' text-anchor='middle' font-family='sans-serif' font-size='13'")
			.append(isCurrent ? " font-weight='bold'" : "")
			.append(">")
			.append(escape(r.id)).append(" &middot; ").append(escape(r.name))
			.append("</text>\n");
		}
		
		svg.append("</svg>\n");
		
		String js = buildJs(width, height, currentRoomId, currentRoomState, 800);
		String html =
		"<!doctype html><html><head><meta charset='utf-8'>" +
		"<meta name='viewport' content='width=device-width, initial-scale=1'>" +
		"<title>Room graph</title>" +
		"<style>" +
		"  html, body { height:100%; margin:0; font-family:sans-serif; background:#fafafa; }" +
		"  body { display:flex; flex-direction:column; }" +
		"  #toolbar { flex:0 0 auto; padding:8px; background:#fff; border-bottom:1px solid #ddd;" +
		"             display:flex; gap:6px; flex-wrap:wrap; }" +
		"  #toolbar button { padding:6px 12px; font-size:13px; border:1px solid #bbb;" +
		"                    border-radius:6px; background:#f7f7f7; }" +
		"  #toolbar button:active { background:#e0e0e0; }" +
		"  #wrap { flex:1 1 0%; min-height:0; overflow:hidden; position:relative; }" +
		"  svg { display:block; width:100%; height:100%; touch-action:none;" +
		"        user-select:none; -webkit-user-select:none; }" +
		"  rect.node { cursor:pointer; }" +
		"</style></head><body>" +
		"<div id='toolbar'>" +
		"  <button onclick='zoomIn()'>+</button>" +
		"  <button onclick='zoomOut()'>&minus;</button>" +
		"  <button onclick='resetView()'>Reset</button>" +
		"  <button onclick='goToCurrent()'>Center current</button>" +
		"</div>" +
		"<div id='wrap'>" + svg + "</div>" +
		"<script>" + js + "</script>" +
		"</body></html>";
		
		return html;
	}
	/**
* Builds the JavaScript block for the room graph page.
*
* @param width         SVG viewBox width
* @param height        SVG viewBox height
* @param currentRoomId id to highlight initially; may be null
* @param initialViewH  height of the initial visible window in SVG units
*                      (pass height for "fit all", smaller for more zoom)
*/	
	static String buildJs(int width, int height,
	String currentRoomId,
	String currentRoomState,   // NEW: "completed", "normal", or null
	int initialViewH) {
		String id = currentRoomId == null ? "" : escapeJs(currentRoomId);
		String state = currentRoomState == null ? "" : escapeJs(currentRoomState);
		
		StringBuilder b = new StringBuilder(6144);
		
		b.append("(function(){\n");
		b.append("  var svg = document.querySelector('svg');\n");
		b.append("  if(!svg) return;\n");
		b.append("  svg.setAttribute('preserveAspectRatio','xMidYMid slice');\n");
		b.append("\n");
		b.append("  // ---- view state --------------------------------------------------\n");
		b.append("  var LIMIT_W = ").append(width).append(", LIMIT_H = ").append(height).append(";\n");
		b.append("  var TAP_SLOP_PX = 20;   // screen px; ~Android's default touch slop\n");
		b.append("  var orig = {x:0, y:0, w:").append(width)
		.append(", h:").append(Math.max(1, initialViewH)).append("};\n");
		b.append("  var vb   = {x:orig.x, y:orig.y, w:orig.w, h:orig.h};\n");
		b.append("  var currentId   = '").append(id).append("';\n");
		b.append("  var currentState = '").append(state).append("';\n");
		b.append("\n");
		b.append("  function apply(){\n");
		b.append("    svg.setAttribute('viewBox', vb.x+' '+vb.y+' '+vb.w+' '+vb.h);\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  // With preserveAspectRatio='xMidYMid slice', the visible region\n");
		b.append("  // is a centered sub-box of vb. This returns its size and its offset\n");
		b.append("  // from vb.x / vb.y, in viewBox units.\n");
		b.append("  function visibleBox(){\n");
		b.append("    var rect = svg.getBoundingClientRect();\n");
		b.append("    if(rect.width <= 0 || rect.height <= 0){\n");
		b.append("      return {w:vb.w, h:vb.h, offX:0, offY:0};\n");
		b.append("    }\n");
		b.append("    var scale = Math.max(rect.width / vb.w, rect.height / vb.h);\n");
		b.append("    var visW = rect.width  / scale;\n");
		b.append("    var visH = rect.height / scale;\n");
		b.append("    return {\n");
		b.append("      w: visW,\n");
		b.append("      h: visH,\n");
		b.append("      offX: (vb.w - visW) / 2,\n");
		b.append("      offY: (vb.h - visH) / 2\n");
		b.append("    };\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  function clamp(){\n");
		b.append("    // zoom limits\n");
		b.append("    if(vb.w > LIMIT_W) vb.w = LIMIT_W;\n");
		b.append("    if(vb.h > LIMIT_H) vb.h = LIMIT_H;\n");
		b.append("    if(vb.w < 80) vb.w = 80;\n");
		b.append("    if(vb.h < 80) vb.h = 80;\n");
		b.append("\n");
		b.append("    var v = visibleBox();\n");
		b.append("\n");
		b.append("    // Horizontal\n");
		b.append("    if(v.w >= LIMIT_W){\n");
		b.append("      vb.x = (LIMIT_W - vb.w) / 2;\n");
		b.append("    } else {\n");
		b.append("      var minX = -v.offX;\n");
		b.append("      var maxX = LIMIT_W - v.offX - v.w;\n");
		b.append("      if(vb.x < minX) vb.x = minX;\n");
		b.append("      if(vb.x > maxX) vb.x = maxX;\n");
		b.append("    }\n");
		b.append("\n");
		b.append("    // Vertical\n");
		b.append("    if(v.h >= LIMIT_H){\n");
		b.append("      vb.y = (LIMIT_H - vb.h) / 2;\n");
		b.append("    } else {\n");
		b.append("      var minY = -v.offY;\n");
		b.append("      var maxY = LIMIT_H - v.offY - v.h;\n");
		b.append("      if(vb.y < minY) vb.y = minY;\n");
		b.append("      if(vb.y > maxY) vb.y = maxY;\n");
		b.append("    }\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  // ---- buttons -----------------------------------------------------\n");
		b.append("  window.zoomIn  = function(){ zoom(0.6); };\n");
		b.append("  window.zoomOut = function(){ zoom(1.65); };\n");
		b.append("  window.resetView = function(){\n");
		b.append("    vb = {x:orig.x, y:orig.y, w:orig.w, h:orig.h};\n");
		b.append("    clamp(); apply();\n");
		b.append("  };\n");
		b.append("  window.goToCurrent = function(){ centerOn(currentId); };\n");
		b.append("\n");
		b.append("  function zoom(f){\n");
		b.append("    var cx = vb.x + vb.w/2, cy = vb.y + vb.h/2;\n");
		b.append("    vb.w *= f; vb.h *= f;\n");
		b.append("    vb.x = cx - vb.w/2; vb.y = cy - vb.h/2;\n");
		b.append("    clamp(); apply();\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  function centerOn(id){\n");
		b.append("    if(!id) return;\n");
		b.append("    var el = svg.querySelector(\"rect.node[data-id='\" + id + \"']\");\n");
		b.append("    if(!el) return;\n");
		b.append("    var x = parseFloat(el.getAttribute('x'));\n");
		b.append("    var y = parseFloat(el.getAttribute('y'));\n");
		b.append("    var w = parseFloat(el.getAttribute('width'));\n");
		b.append("    var h = parseFloat(el.getAttribute('height'));\n");
		b.append("    vb.x = x + w/2 - vb.w/2;\n");
		b.append("    vb.y = y + h/2 - vb.h/2;\n");
		b.append("    clamp(); apply();\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  // ---- highlight current room --------------------------------------\n");
		b.append("  var FILL_CURRENT_NORMAL    = '#ffe9a8';\n");
		b.append("  var FILL_CURRENT_COMPLETED = '#a8ffa8';\n");
		b.append("  var STROKE_CURRENT         = '#c77800';\n");
		b.append("  var FILL_NORMAL            = '#e6f0ff';\n");
		b.append("  var STROKE_NORMAL          = '#3c5a96';\n");
		b.append("\n");
		b.append("  function setCurrentRoom(id, state){\n");
		b.append("    currentId    = id    || '';\n");
		b.append("    if(state !== undefined) currentState = state || '';\n");
		b.append("    var currentFill = (currentState === 'completed')\n");
		b.append("                      ? FILL_CURRENT_COMPLETED\n");
		b.append("                      : FILL_CURRENT_NORMAL;\n");
		b.append("    var nodes = svg.querySelectorAll('rect.node');\n");
		b.append("    for(var i=0;i<nodes.length;i++){\n");
		b.append("      var el = nodes[i];\n");
		b.append("      var on = el.getAttribute('data-id') === currentId;\n");
		b.append("      el.setAttribute('fill',         on ? currentFill   : FILL_NORMAL);\n");
		b.append("      el.setAttribute('stroke',       on ? STROKE_CURRENT: STROKE_NORMAL);\n");
		b.append("      el.setAttribute('stroke-width', on ? '3' : '1');\n");
		b.append("    }\n");
		b.append("  }\n");
		b.append("  window.setCurrentRoom = setCurrentRoom;\n");
		b.append("\n");
		b.append("  // ---- drag to pan -------------------------------------------------\n");
		b.append("  var dragging = false, moved = false;\n");
		b.append("  var startX = 0, startY = 0, startVbX = 0, startVbY = 0;\n");
		b.append("  var activeId = null;\n");
		b.append("\n");
		b.append("  function pointFromEvent(e){\n");
		b.append("    if(e.touches && e.touches.length) return {x:e.touches[0].clientX, y:e.touches[0].clientY};\n");
		b.append("    return {x:e.clientX, y:e.clientY};\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  function onDown(e){\n");
		b.append("    if(e.touches && e.touches.length > 1) return;   // let pinch handler take over\n");
		b.append("    dragging = true; moved = false;\n");
		b.append("    var p = pointFromEvent(e);\n");
		b.append("    startX = p.x; startY = p.y;\n");
		b.append("    startVbX = vb.x; startVbY = vb.y;\n");
		b.append("    activeId = (e.pointerId !== undefined) ? e.pointerId : null;\n");
		b.append("    if(activeId !== null && svg.setPointerCapture){\n");
		b.append("      try { svg.setPointerCapture(activeId); } catch(_) {}\n");
		b.append("    }\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  function onMove(e){\n");
		b.append("    if(!dragging) return;\n");
		b.append("    var p = pointFromEvent(e);\n");
		b.append("    var rect = svg.getBoundingClientRect();\n");
		b.append("    var sx = vb.w / rect.width;\n");
		b.append("    var sy = vb.h / rect.height;\n");
		b.append("    var dx = (p.x - startX) * sx;\n");
		b.append("    var dy = (p.y - startY) * sy;\n");
		b.append("    if(Math.abs(dx) > TAP_SLOP_PX || Math.abs(dy) > TAP_SLOP_PX) moved = true;\n");
		b.append("    vb.x = startVbX - dx;\n");
		b.append("    vb.y = startVbY - dy;\n");
		b.append("    clamp(); apply();\n");
		b.append("    if(e.cancelable) e.preventDefault();\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  function onUp(e){\n");
		b.append("    if(!dragging) return;\n");
		b.append("    dragging = false;\n");
		b.append("    if(activeId !== null && svg.releasePointerCapture){\n");
		b.append("      try { svg.releasePointerCapture(activeId); } catch(_) {}\n");
		b.append("    }\n");
		b.append("    activeId = null;\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  svg.addEventListener('pointerdown', onDown);\n");
		b.append("  svg.addEventListener('pointermove', onMove);\n");
		b.append("  svg.addEventListener('pointerup', onUp);\n");
		b.append("  svg.addEventListener('pointercancel', onUp);\n");
		b.append("  svg.addEventListener('pointerleave', onUp);\n");
		b.append("\n");
		b.append("  // Touch fallback for WebViews without pointer events\n");
		b.append("  if(!window.PointerEvent){\n");
		b.append("    svg.addEventListener('touchstart', onDown, {passive:true});\n");
		b.append("    svg.addEventListener('touchmove',  onMove, {passive:false});\n");
		b.append("    svg.addEventListener('touchend',   onUp,   {passive:true});\n");
		b.append("    svg.addEventListener('touchcancel',onUp,   {passive:true});\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  // ---- pinch to zoom ----------------------------------------------\n");
		b.append("  var pinchDist = 0, pinchStartW = 0, pinchStartH = 0;\n");
		b.append("  svg.addEventListener('touchstart', function(e){\n");
		b.append("    if(e.touches.length === 2){\n");
		b.append("      dragging = false;\n");
		b.append("      pinchDist = touchDist(e);\n");
		b.append("      pinchStartW = vb.w; pinchStartH = vb.h;\n");
		b.append("    }\n");
		b.append("  }, {passive:true});\n");
		b.append("  svg.addEventListener('touchmove', function(e){\n");
		b.append("    if(e.touches.length !== 2 || pinchDist === 0) return;\n");
		b.append("    var d = touchDist(e);\n");
		b.append("    var f = pinchDist / d;\n");
		b.append("    var cx = vb.x + vb.w/2, cy = vb.y + vb.h/2;\n");
		b.append("    vb.w = pinchStartW * f;\n");
		b.append("    vb.h = pinchStartH * f;\n");
		b.append("    vb.x = cx - vb.w/2; vb.y = cy - vb.h/2;\n");
		b.append("    clamp(); apply();\n");
		b.append("    if(e.cancelable) e.preventDefault();\n");
		b.append("  }, {passive:false});\n");
		b.append("  svg.addEventListener('touchend', function(e){\n");
		b.append("    if(e.touches.length < 2) pinchDist = 0;\n");
		b.append("  }, {passive:true});\n");
		b.append("\n");
		b.append("  function touchDist(e){\n");
		b.append("    var dx = e.touches[0].clientX - e.touches[1].clientX;\n");
		b.append("    var dy = e.touches[0].clientY - e.touches[1].clientY;\n");
		b.append("    return Math.sqrt(dx*dx + dy*dy);\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  // ---- room tap ---------------------------------------------------\n");
		b.append("  var nodes = svg.querySelectorAll('rect.node');\n");
		b.append("  for(var i=0;i<nodes.length;i++){\n");
		b.append("    (function(el){\n");
		b.append("      el.addEventListener('click', function(){\n");
		b.append("        if(moved) return;\n");
		b.append("        var rid = el.getAttribute('data-id');\n");
		b.append("        //set current room handled by game logic\n");
		b.append("        //setCurrentRoom(rid);\n");
		b.append("        if(window.Android && window.Android.onRoomSelected){\n");
		b.append("          window.Android.onRoomSelected(rid);\n");
		b.append("        }\n");
		b.append("      });\n");
		b.append("    })(nodes[i]);\n");
		b.append("  }\n");
		b.append("\n");
		b.append("  // ---- initial paint ----------------------------------------------\n");
		b.append("  // Re-anchor orig.y to the map's top once we know the real viewport.\n");
		b.append("  var v0 = visibleBox();\n");
		b.append("  orig.y = -v0.offY;\n");
		b.append("  vb.y   = orig.y;\n");
		b.append("  if(currentId) setCurrentRoom(currentId, currentState);\n");
		b.append("  clamp(); apply();\n");
		b.append("\n");
		b.append("  // Re-clamp on orientation change / dialog resize.\n");
		b.append("  window.addEventListener('resize', function(){ clamp(); apply(); });\n");
		b.append("})();\n");
		
		return b.toString();
	}
	
	/** Escapes a Java string for embedding inside a single-quoted JS string. */
	private static String escapeJs(String s) {
		StringBuilder o = new StringBuilder(s.length() + 8);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case '\\': o.append("\\\\"); break;
				case '\'': o.append("\\'");  break;
				case '\n': o.append("\\n");  break;
				case '\r': o.append("\\r");  break;
				case '<':  o.append("\\x3c"); break;   // avoid closing </script>
				case '>':  o.append("\\x3e"); break;
				case '&':  o.append("\\x26"); break;
				default:   o.append(c);
			}
		}
		return o.toString();
	}
	
	static String escape(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}
	
	public static void main(String[] args) {
		Map<String, Object> parsed = new LinkedHashMap<>();
		// ... populate with your library's parse result ...
		// write(parsed, "rooms.html");
	}
	
	static class Room {
		String name, id;
		List<String> nextIds = new ArrayList<>();
	}
	
	/** Build the graph from an already-parsed Map (same shape as before). */
	// need the parsed map under "rooms"
	static Map<String, Room> buildGraph(List<Map<String, Object>> roomsList) {
		List<Map<String, Object>> list = roomsList;
		
		Map<String, Room> rooms = new LinkedHashMap<>();
		for (Map<String, Object> r : list) {
			Room room = new Room();
			room.name = String.valueOf(r.get("name"));
			room.id   = String.valueOf(r.get("id"));
			
			Object nextObj = r.get("next_id");
			if (nextObj instanceof List) {
				for (Object o : (List<?>) nextObj) room.nextIds.add(String.valueOf(o));
			} else if (nextObj instanceof String) {
				room.nextIds.addAll(parseIds((String) nextObj));
			}
			rooms.put(room.id, room);
		}
		return rooms;
	}
	
	static List<String> parseIds(String s) {
		List<String> out = new ArrayList<>();
		int i = 0;
		while (i < s.length()) {
			int q1 = s.indexOf('"', i);
			if (q1 < 0) break;
			int q2 = s.indexOf('"', q1 + 1);
			if (q2 < 0) break;
			out.add(s.substring(q1 + 1, q2));
			i = q2 + 1;
		}
		return out;
	}
	
	/**
* Assign levels by BFS from the root. Rooms not reachable from the root
* are placed on their own row at the bottom.
*
* @param rootId id to start BFS from; if null or missing, the first room
*               in the list is used.
*/	
	static SortedMap<Integer, List<Room>> byLevel(Map<String, Room> rooms, String rootId) {
		// Pick a default root: first room in insertion order.
		if (rootId == null || !rooms.containsKey(rootId)) {
			if (rooms.isEmpty()) return new TreeMap<>();
			rootId = rooms.keySet().iterator().next();
		}
		
		Map<String, Integer> level = new HashMap<>();
		Deque<String> queue = new ArrayDeque<>();
		level.put(rootId, 0);
		queue.add(rootId);
		
		while (!queue.isEmpty()) {
			String id = queue.poll();
			Room r = rooms.get(id);
			if (r == null) continue;
			int nextLevel = level.get(id) + 1;
			for (String n : r.nextIds) {
				if (!rooms.containsKey(n)) continue;      // dangling edge, skip
				if (level.putIfAbsent(n, nextLevel) == null) {
					queue.add(n);
				}
			}
		}
		
		// Anything not reached from the root goes into a single "orphan" row.
		final int ORPHAN = Integer.MAX_VALUE;
		for (String id : rooms.keySet()) {
			level.putIfAbsent(id, ORPHAN);
		}
		
		SortedMap<Integer, List<Room>> result = new TreeMap<>();
		for (Room r : rooms.values()) {
			int lvl = level.getOrDefault(r.id, ORPHAN);
			result.computeIfAbsent(lvl, k -> new ArrayList<>()).add(r);
		}
		for (List<Room> lvl : result.values()) {
			lvl.sort(Comparator.comparing(r -> r.id));
		}
		return result;
	}
}
