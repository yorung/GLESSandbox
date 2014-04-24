precision mediump float;
varying vec2 position;
varying vec2 texcoord;
uniform sampler2D sampler1;
uniform sampler2D sampler2;
uniform float time;
void main() {
//	vec2 coord = vec2(texcoord.x + sin(texcoord.x * 25.0 + time * 2.0) / 200.0, texcoord.y + sin(texcoord.y * 25.0 + time * 3.0) / 200.0);  

	float dist1 = length(position + vec2(0.5, 0.5));
	float dist2 = length(position - vec2(0.5, 0.5));
	vec2 coord = vec2(texcoord.x, texcoord.y
		+ sin(dist1 * 8.7 + time * 8.0) / 800.0
		+ sin(dist2 * 10.0 + time * 12.5) / 800.0);

	float alpha = sin(time * 3.14 / 5.0) / 2.0 + 0.5;
	vec4 c1 = texture2D(sampler1, coord);
	vec4 c2 = texture2D(sampler2, coord);
	gl_FragColor = mix(c1, c2, alpha);
}
