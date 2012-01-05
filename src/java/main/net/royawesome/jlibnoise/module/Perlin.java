package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.Noise;
import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.Utils;

public class Perlin extends Module {

	/// Default frequency for the noise::module::Perlin noise module.
	public static final double DEFAULT_PERLIN_FREQUENCY = 1.0;

	/// Default lacunarity for the noise::module::Perlin noise module.
	public static final double DEFAULT_PERLIN_LACUNARITY = 2.0;

	/// Default number of octaves for the noise::module::Perlin noise module.
	public static final int DEFAULT_PERLIN_OCTAVE_COUNT = 6;

	/// Default persistence value for the noise::module::Perlin noise module.
	public static final double DEFAULT_PERLIN_PERSISTENCE = 0.5;

	/// Default noise quality for the noise::module::Perlin noise module.
	public static final NoiseQuality DEFAULT_PERLIN_QUALITY = NoiseQuality.STANDARD;

	/// Default noise seed for the noise::module::Perlin noise module.
	public static final int DEFAULT_PERLIN_SEED = 0;

	/// Maximum number of octaves for the noise::module::Perlin noise module.
	public static final int PERLIN_MAX_OCTAVE = 30;

	/// Frequency of the first octave.
	double frequency = DEFAULT_PERLIN_FREQUENCY;

	/// Frequency multiplier between successive octaves.
	double lacunarity = DEFAULT_PERLIN_LACUNARITY;

	/// Quality of the Perlin noise.
	NoiseQuality noiseQuality = DEFAULT_PERLIN_QUALITY;

	/// Total number of octaves that generate the Perlin noise.
	int octaveCount = DEFAULT_PERLIN_OCTAVE_COUNT;

	/// Persistence of the Perlin noise.
	double persistence = DEFAULT_PERLIN_PERSISTENCE;

	/// Seed value used by the Perlin-noise function.
	int seed = DEFAULT_PERLIN_SEED;

	public Perlin() {
		super(0);
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public double getLacunarity() {
		return lacunarity;
	}

	public void setLacunarity(double lacunarity) {
		this.lacunarity = lacunarity;
	}

	public NoiseQuality getNoiseQuality() {
		return noiseQuality;
	}

	public void setNoiseQuality(NoiseQuality noiseQuality) {
		this.noiseQuality = noiseQuality;
	}

	public int getOctaveCount() {
		return octaveCount;
	}

	public void setOctaveCount(int octaveCount) {
		if (octaveCount < 1 || octaveCount > PERLIN_MAX_OCTAVE) {
			throw new IllegalArgumentException("octaveCount must be between 1 and MAX OCTAVE: " + PERLIN_MAX_OCTAVE);
		}

		this.octaveCount = octaveCount;
	}

	public double getPersistence() {
		return persistence;
	}

	public void setPersistence(double persistence) {
		this.persistence = persistence;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	@Override
	public int GetSourceModuleCount() {
		return 0;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		double value = 0.0;
		double signal = 0.0;
		double curPersistence = 1.0;
		double nx, ny, nz;
		int seed;

		x *= frequency;
		y *= frequency;
		z *= frequency;

		for (int curOctave = 0; curOctave < octaveCount; curOctave++) {

			// Make sure that these floating-point values have the same range as a 32-
			// bit integer so that we can pass them to the coherent-noise functions.
			nx = Utils.MakeInt32Range (x);
			ny = Utils.MakeInt32Range (y);
			nz = Utils.MakeInt32Range (z);

			// Get the coherent-noise value from the input value and add it to the
			// final result.
			seed = (this.seed + curOctave) & 0xffffffff;
			signal = Noise.GradientCoherentNoise3D (nx, ny, nz, seed, noiseQuality);
			value += signal * curPersistence;

			// Prepare the next octave.
			x *= lacunarity;
			y *= lacunarity;
			z *= lacunarity;
			curPersistence *= persistence;
		}

		return value;

	}

}
