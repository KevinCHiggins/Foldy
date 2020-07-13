# Foldy 1.0
### Wavefolding Synth-Sequencer using Java Sound API, in fixed-point arithmetic

#### 1.0 release notes

Foldy is a (very) digital non-realtime synth-sequencer using integer arithmetic and standard Java libraries. This release fixes errors with waveforms and certain note lengths, and provide a cross-platform graphical interface built in Swing. Features:

- Modify harmonic content by folding sine waves
- Set the speed of performances using the musical variables of tempo and subdivision
- Type in any melodic sequence you want using MIDI note numbers
- Save audio to disk in raw format

Some caveats... waveforms other than sines don't transform nicely under folding; tempos use fixed subdivisions rounded to the nearest sample and so may not precisely match target BPM.

There is only a linear fall-off amplitude envelope at the moment.

Looking back, I can see that this project was heavily inspired by two venerable and excellent PC music software releases: [Hammerhead Rhythm Station](http://threechords.com/hammerhead/introduction.shtml) and [Drumsynth](http://mda.smartelectronix.com/drumsynth.htm). From Hammerhead I got the idea of saving raw files to disk, and from Drumsynth the whole idea of non-realtime rendering.

I'm happy enough with the code considering my experience level, apart from the Chunk system which was a poor idea that got heavily patched over. ```Note.java```, where all that happens, is ugly.

As I said in the previous release notes, there's no point continuing with this design from a musical standpoint. I'm glad however that I made the GUI because it embodies a simple design where the form is separate from the rendering, with a single function call being the point of interchange (```Manifester.bust```). This is inspired by reading Drumsynth's source code.

Kevin Higgins, 08/07/20

#### Previous beta release notes

Wrapping this project up today with a couple of example sounds and this readme. The design choices - particularly the use of an unchangingly repeated Chunk containing a small whole number of cycles of the fundamental frequency, as the basic subunit of a note, are too restrictive to make further development worthwhile.

For one thing, they limit frequency accuracy to within something like 20 cents. I thought that might be a musical creative limitation but it rules out much interesting additive synthesis stuff, and pitchbends.

Finally, the non-realtime aspect helped me get it off the ground but it's again too basic to keep working on.

Most interesting thing I learned was approximating a sine wave with a truncated power series.

Kevin Higgins, 24/05/20

Examples:

[Blues](https://raw.githubusercontent.com/KevinCHiggins/Foldy/master/Blues.mp3)

[Subharmonics and harmonics](https://raw.githubusercontent.com/KevinCHiggins/Foldy/master/Harmonics.mp3)
