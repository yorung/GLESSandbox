precision mediump float;
varying vec2 texcoord;
uniform sampler2D sampler1;
uniform sampler2D sampler2;
uniform float time;
void main() {
	vec2 coord = vec2(texcoord.x + sin(texcoord.x * 50.0 + time * 2.0) / 100.0, texcoord.y + sin(texcoord.y * 50.0 + time * 3.0) / 100.0);  
	float alpha = sin(time * 3.14 / 5.0) / 2.0 + 0.5;
	vec4 c1 = texture2D(sampler1, coord);
	vec4 c2 = texture2D(sampler2, coord);
	gl_FragColor = mix(c1, c2, alpha);
}
