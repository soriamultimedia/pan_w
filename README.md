Original Multichannel spatialization algorithm for Supercollider developed through mathematical analysis of sinusoidal amplitude panning tecnique. 
Research project. Master Degree Music Technology.

# PanW (SuperCollider) — Arbitrary Multichannel Trajectory Panner

PanW is a SuperCollider panning class designed for **multichannel spatial sound trajectories** where the trajectory is defined by an **arbitrary ordered list of outputs** (a `path` array), rather than being constrained to **adjacent-speaker motion** (as in many stock panners).  
The approach is based on **cosine amplitude panning**, with a mathematically modeled “spread” parameter controlling how many loudspeakers are active simultaneously.

> Reference context (paper): many official SC panners only allow transitions through adjacent outputs; PanW was designed to overcome that limitation with minimal extra CPU cost.  
> (See the accompanying paper for the full rationale and model.)

---

## Features

- **Arbitrary trajectories:** define any ordering of outputs with a `path` array.
- **Continuous position control:** `pos ∈ [0,1]` moves from the first to the last element of `path`.
- **Spread control:** wider diffusion by increasing the spread parameter (`offset` / `k`).
- **Lightweight:** comparable CPU to stock panners in the paper’s benchmark.

---

## Requirements

- SuperCollider 3.x
- Audio interface / routing configured with enough output channels for your target setup.

---

## Installation (as an Extension)

1. Create a folder in your SuperCollider Extensions directory, for example:

   **macOS (user):**
   `~/Library/Application Support/SuperCollider/Extensions/PanW/`

   **Windows (user):**
   `%AppData%/SuperCollider/Extensions/PanW/`

   **Linux (user):**
   `~/.local/share/SuperCollider/Extensions/PanW/`

2. Put the class file inside that folder and name it clearly, e.g.:

   `PanW.sc`

   > The class defined inside is `PanW`. The filename doesn’t strictly have to match,
   > but matching names helps maintenance.

3. Recompile the class library:
   - In the IDE: **Language → Recompile Class Library**
   - Shortcut (commonly): `Ctrl+Shift+L` (Win/Linux) / `Cmd+Shift+L` (macOS)

4. Confirm it loads:
   ```supercollider
   PanW.dumpInterface


Quick start examples
1) Manual trajectory control (Mouse)
   
(
SynthDef(\panw_mouse, {
    var src  = PinkNoise.ar(0.1);
    var pos  = MouseX.kr(0, 1);
    var k    = 4;
    var path = [0,1,2,3,4,5,6,7];
    PanW.ar(src, pos, k, path, level: 0.7);
}).add;
)

x = Synth(\panw_mouse);

2) Arbitrary “jump” trajectory (non-adjacent routing)

(
SynthDef(\panw_path, {
    var src  = SinOsc.ar(220, 0, 0.1);
    var pos  = LFSaw.kr(0.07).range(0, 1);
    var k    = 5;
    var path = [0, 3, 7, 1, 2, 4];
    PanW.ar(src, pos, k, path, level: 0.7);
}).add;
)

x = Synth(\panw_path);


(
~logisticOrbit = { |x0=0.5, r=3.9, n=512|
    var x = x0;
    Array.fill(n, { x = r*x*(1-x); x.clip(0,1) });
};
)

3) Data-driven trajectories (logistic map orbit → pos)

(
SynthDef(\panw_orbit, { |pos=0|
    var src  = WhiteNoise.ar(0.08);
    var k    = 4;
    var path = [0,5,2,1,7,6,3,4];
    PanW.ar(src, pos, k, path, level: 0.7);
}).add;
)

(
~orbit = ~logisticOrbit.(0.5, 3.9, 800);
x = Synth(\panw_orbit);

Routine({
    ~orbit.do { |v|
        x.set(\pos, v);
        0.02.wait;
    };
}).play;
)



Shield: [![CC BY-NC-SA 4.0][cc-by-nc-sa-shield]][cc-by-nc-sa]

This work is licensed under a
[Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License][cc-by-nc-sa].

[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]

[cc-by-nc-sa]: http://creativecommons.org/licenses/by-nc-sa/4.0/
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png
[cc-by-nc-sa-shield]: https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-lightgrey.svg
