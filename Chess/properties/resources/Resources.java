/**
* Daniel Ricci <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package resources;

import engine.utils.globalisation.Localisation;

public final class Resources extends Localisation<Resources.ResourceKeys> {
	
	private static Resources _instance;
	
	private Resources(){
	}
	
	public enum ResourceKeys {
		Title("title");

		private String _key;
		ResourceKeys(String resourceKey) {
			_key = resourceKey;
		}
		@Override public String toString() {
			return _key;
		}
	}
	
	public static Resources instance(){
		if(_instance == null) {
			_instance = new Resources();
		}
		return _instance;
	}
}