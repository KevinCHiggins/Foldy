# Foldy
Wavefolding Synth-Sequencer using Java Sound API

Wrapping this project up today with a couple of example sounds and this readme. The design choices - particularly the use of an unchangingly repeated Chunk containing a small whole number of cycles of the fundamental frequency, as the basic subunit of a note, are too restrictive to make further development worthwhile.

For one thing, they limit frequency accuracy to within something like 20 cents. I thought that might be a musical creative limitation but it rules out much interesting additive synthesis stuff, and pitchbends.

Finally, the non-realtime aspect helped me get it off the ground but it's again too basic to keep working on.

Most interesting thing I learned was approximating a sine wave with a truncated power series.

Kevin Higgins, 24/05/20

Examples:

[Blues](https://raw.githubusercontent.com/KevinCHiggins/Foldy/master/Harmonics.wav)

[Subharmonics and harmonics](https://raw.githubusercontent.com/KevinCHiggins/Foldy/master/Harmonics.wav)
